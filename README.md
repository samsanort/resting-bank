# resting-bank
A (very simple) RESTful banking backend.

**Available operations:**

Register a new user:

`POST: /users`

[args] (JSON)
`{"email":"<the email>", "password":"<the password>"
}`

[returns] (JSON)
`{"id":<registered user Id>, "accountId": <users account id>}`

Make a deposit:

`POST: /accounts/<accountId>/deposits`

[args] (text)
`<deposit amount>`

Withdraw:

`POST: /accounts/<accountId>/withdrawals`

[args] (text)
`<withdrawal amount>`

Obtain account statement:

`GET: /accounts/<accountId>`

[returns] (JSON)
`{"date":"<statement date>", "balance":<account balance>, "transactions":[
{"date":"<transaction date>", "amount":<transaction amount>, "description":<transaction description>},..]}`


Note that, except for the "register user" entry point, the rest of URLs are secured with basic HTTP authentication; This means that the registered user email and password must be sent along the request as a header.