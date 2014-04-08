<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
		<g:layoutHead/>
		<r:layoutResources />
	</head>
	<body>
		<div id="grailsLogo" role="banner"><a href="${request.contextPath}"><img src="${resource(dir: 'images', file: 'grails_logo.png')}" alt="s2o"/></a></div>
        <div>
          <br>
            <oauth:connect provider="facebook" id="facebook-connect-link">facebook</oauth:connect> | 
            <oauth:connect provider="twitter" id="twitter-connect-link">twitter</oauth:connect> | 
            <oauth:connect provider="google" id="google-connect-link">google</oauth:connect> | 
            <oauth:connect provider="linkedin" id="linkedin-connect-link">linkedin</oauth:connect>
          <br>
            Logged with facebook? <s2o:ifLoggedInWith provider="facebook">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="facebook">no</s2o:ifNotLoggedInWith> |
            Logged with twitter? <s2o:ifLoggedInWith provider="twitter">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="twitter">no</s2o:ifNotLoggedInWith> |
            Logged with google? <s2o:ifLoggedInWith provider="google">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="google">no</s2o:ifNotLoggedInWith> |
            Logged with linkedin? <s2o:ifLoggedInWith provider="linkedin">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="linkedin">no</s2o:ifNotLoggedInWith> |
          <br>
            <sec:ifLoggedIn>
                Logged in as <sec:username/>
                <form name="logout" action="${request.contextPath}/logout" method="POST"><input type="submit" value="Logout !"></form>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <g:link controller='login' action='auth'>Login</g:link>
            </sec:ifNotLoggedIn>
          <br>
        </div>
		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<g:javascript library="application"/>
		<r:layoutResources />
	</body>
</html>
