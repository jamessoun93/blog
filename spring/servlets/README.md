# Servlets

## HttpServletRequest 역할

- HTTP 요청 메시지를 파싱해줌.
- 그 결과를 `HttpServletRequest` 객체에 담아서 제공함.
- 임시저장소 기능: 해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능
	- `request.setAttribute(name, value)`
	- `request.getAttribute(name)`
- 세션 관리 기능
	- `request.getSession(create: true)`
