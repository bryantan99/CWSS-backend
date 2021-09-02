<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Account Creation Mail</title>
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td style="background-color: rgba(245, 245, 245, 255);">
            <table width="800" border="0" cellspacing="50" cellpadding="0" align="center">
                <tr>
                    <td valign="top"
                        style="background-color: white; font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #000000; padding: 2em;">
                        <div>
                            <h1>Aulong Community Service Centre</h1>
                            <p>Hi ${fullName}!</p>
                            <p>Welcome to the team. We're thrilled to have you at the Aulong Community Service Centre.
                                We've prepared a staff account with the login credentials below for you.</p>
                            <p>
                                Username: ${username}<br>
                                Password: ${defaultPw}
                            </p>
                            <p>
                                To login your account, click <a href="http://localhost:4200/login">here</a>. Please
                                remember to change your password after you have logged in with your default password to
                                prevent any unauthorized access from other people.
                            </p>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>