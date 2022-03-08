# 버텍스 봇 만들기

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

app.message('vt', async ({ message, say }) => {
  await say({
    "blocks": [
      {
        "type": "section",
        "text": {
          "type": "mrkdwn",
          "text": ":studio_microphone: (츼직츼직..) 아아",
        }
      },
      {
        "type": "actions",
        "elements": [
          {
            "type": "button",
            "text": {
              "type": "plain_text",
              "text": "탑승하기",
              "emoji": true
            },
            "value": "click_me_123",
            "action_id": "join-vertext"
          }
        ]
      }
    ]
  })
});

app.action('join-vertext', async ({ body, ack, say, client, logger }) => {
  await ack();

  try {
    const result = await client.views.open({
      trigger_id: body.trigger_id,
      view: {
        "title": {
          "type": "plain_text",
          "text": "탑승권 예약",
          "emoji": true
        },
        "submit": {
          "type": "plain_text",
          "text": "확인",
          "emoji": true
        },
        "type": "modal",
        "close": {
          "type": "plain_text",
          "text": "취소",
          "emoji": true
        },
        "callback_id": "order_view",
        "blocks": [
          {
            "type": "input",
            "block_id": "menu",
            "element": {
              "type": "static_select",
              "placeholder": {
                "type": "plain_text",
                "text": "메뉴 선택",
                "emoji": true
              },
              "options": [
                {
                  "text": {
                    "type": "plain_text",
                    "text": "미국식 닭고기 덮밥",
                    "emoji": true
                  },
                  "value": "미국식 닭고기 덮밥"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "미국식 새우 닭고기 덮밥",
                    "emoji": true
                  },
                  "value": "미국식 새우 닭고기 덮밥"
                }
              ],
              "action_id": "static_select-action"
            },
            "label": {
              "type": "plain_text",
              "text": "메뉴 선택 (필수)",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "size",
            "element": {
              "type": "static_select",
              "placeholder": {
                "type": "plain_text",
                "text": "사이즈 선택",
                "emoji": true
              },
              "options": [
                {
                  "text": {
                    "type": "plain_text",
                    "text": "S (점심 한끼 괜찮은 정도)",
                    "emoji": true
                  },
                  "value": "S"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "M (점심 한끼 배부른 정도)",
                    "emoji": true
                  },
                  "value": "M"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "L (점심, 저녁 나눠먹기 좋은 정도)",
                    "emoji": true
                  },
                  "value": "L"
                }
              ],
              "action_id": "static_select-action"
            },
            "label": {
              "type": "plain_text",
              "text": "사이즈 선택 (필수)",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "sauce",
            "element": {
              "type": "static_select",
              "placeholder": {
                "type": "plain_text",
                "text": "소스 선택",
                "emoji": true
              },
              "options": [
                {
                  "text": {
                    "type": "plain_text",
                    "text": "데리야끼",
                    "emoji": true
                  },
                  "value": "데리야끼"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "페퍼",
                    "emoji": true
                  },
                  "value": "페퍼"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "염염",
                    "emoji": true
                  },
                  "value": "염염"
                }
              ],
              "action_id": "static_select-action"
            },
            "label": {
              "type": "plain_text",
              "text": "소스 선택 (필수)",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "option",
            "optional": true,
            "element": {
              "type": "static_select",
              "placeholder": {
                "type": "plain_text",
                "text": "추가 선택",
                "emoji": true
              },
              "options": [
                {
                  "text": {
                    "type": "plain_text",
                    "text": "밥 100g",
                    "emoji": true
                  },
                  "value": "밥 100g"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "염염 소스",
                    "emoji": true
                  },
                  "value": "염염 소스"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "데리야끼 소스",
                    "emoji": true
                  },
                  "value": "데리야끼 소스"
                }
              ],
              "action_id": "static_select-action"
            },
            "label": {
              "type": "plain_text",
              "text": "추가 선택 (Optional)",
              "emoji": true
            }
          },
          {
            "type": "input",
            "block_id": "drink",
            "optional": true,
            "element": {
              "type": "static_select",
              "placeholder": {
                "type": "plain_text",
                "text": "음료 선택",
                "emoji": true
              },
              "options": [
                {
                  "text": {
                    "type": "plain_text",
                    "text": "코카콜라",
                    "emoji": true
                  },
                  "value": "코카콜라"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "제로콜라",
                    "emoji": true
                  },
                  "value": "제로콜라"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "스프라이트",
                    "emoji": true
                  },
                  "value": "스프라이트"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "닥터페퍼",
                    "emoji": true
                  },
                  "value": "닥터페퍼"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "웰치스(포도)",
                    "emoji": true
                  },
                  "value": "웰치스(포도)"
                },
                {
                  "text": {
                    "type": "plain_text",
                    "text": "마운틴듀",
                    "emoji": true
                  },
                  "value": "마운틴듀"
                }
              ],
              "action_id": "static_select-action"
            },
            "label": {
              "type": "plain_text",
              "text": "음료 선택 (Optional)",
              "emoji": true
            }
          }
        ]
      }
    });
  }
  catch (error) {
    logger.error(error);
  }
});

app.view('order_view', async ({ ack, body, view, client, logger }) => {
  await ack();

  const user = body.user.id;
  const menu = view.state.values.menu["static_select-action"].selected_option.value
  const size = view.state.values.size["static_select-action"].selected_option.value
  const sauce = view.state.values.sauce["static_select-action"].selected_option.value
  const option = view.state.values.option["static_select-action"].selected_option?.value
  const drink = view.state.values.drink["static_select-action"].selected_option?.value

  console.log(menu, size, sauce, option, drink)

  try {
    await client.chat.postEphemeral({
      channel: "C015R6X4JCV",
      user: user,
      text: `<@${user}>님은 result를 선택하셨습니다!`
    });
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