# 정산봇 만들기

소스코드
```js
require('dotenv').config()
const { App } = require('@slack/bolt');

const app = new App({
  token: process.env.SLACK_BOT_TOKEN,
  signingSecret: process.env.SLACK_SIGNING_SECRET,
  socketMode: true,
  appToken: process.env.SLACK_APP_TOKEN,
  port: process.env.PORT || 3000
});

app.command('/정산', async ({ ack, body, client, logger }) => {
  await ack();

  try {
    const result = await client.views.open({
      trigger_id: body.trigger_id,
      view: {
        "type": "modal",
        "callback_id": "split_view",
        "title": {
          "type": "plain_text",
          "text": "정산도우미",
          "emoji": true
        },
        "submit": {
          "type": "plain_text",
          "text": "Submit",
          "emoji": true
        },
        "close": {
          "type": "plain_text",
          "text": "Cancel",
          "emoji": true
        },
        "blocks": [
          {
            "type": "input",
            "block_id": "description",
            "element": {
              "type": "plain_text_input",
              "action_id": "plain_text_input-action",
              "placeholder": {
                "type": "plain_text",
                "text": "무엇에 대한 정산인가요?"
              },
            },
            "label": {
              "type": "plain_text",
              "text": "항목",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "total",
            "element": {
              "type": "plain_text_input",
              "action_id": "plain_text_input-action",
              "placeholder": {
                "type": "plain_text",
                "text": "총 금액은 얼마인가요?"
              },
            },
            "label": {
              "type": "plain_text",
              "text": "총 금액",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "account_info",
            "element": {
              "type": "plain_text_input",
              "action_id": "plain_text_input-action",
              "placeholder": {
                "type": "plain_text",
                "text": "은행명과 계좌번호"
              },
            },
            "label": {
              "type": "plain_text",
              "text": "은행명과 계좌번호",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "subjects",
            "element": {
              "type": "multi_users_select",
              "placeholder": {
                "type": "plain_text",
                "text": "대상을 선택해주세요 (본인은 포함하지 말아주세요)",
                "emoji": true
              },
              "action_id": "multi_users_select-action"
            },
            "label": {
              "type": "plain_text",
              "text": "대상 (본인 미포함 전원을 선택해주세요)",
              "emoji": true
            }
          }
        ]
      }
    })
  }
  catch (error) {
    logger.error(error);
  }
});

app.view('split_view', async ({ ack, body, view, client, logger }) => {
  await ack();

  const user = body.user.id;
  const total = view.state.values.total["plain_text_input-action"].value
  const subjects = view.state.values.subjects["multi_users_select-action"].selected_users
  
  const amountDue = total / (subjects.length + 1)
  console.log(total, amountDue)

  try {
    Promise.all(subjects.map(sub => {
      return client.chat.postMessage({
        channel: sub,
        "attachments": [
          {
            "color": "#f2c744",
            "blocks": [
              {
                "type": "header",
                "text": {
                  "type": "plain_text",
                  "text": "🤑 새로운 정산 요청",
                  "emoji": true
                }
              },
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": "*항목*: 점심 버텍스"
                }
              },
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": `*대상*: <@${user}>`
                }
              },
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": "*계좌*: 카뱅 3333-06-12345"
                }
              },
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": `*금액*: ${amountDue.toLocaleString()}원`
                }
              }
            ]
          }
        ]
      })
    }))
  }
  catch (error) {
    logger.error(error);
  }
});

(async () => {
  await app.start();

  console.log('Bolt app is running');
})();
```