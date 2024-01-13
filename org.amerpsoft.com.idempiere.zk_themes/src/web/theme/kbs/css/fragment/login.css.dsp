.login-window {
	background-image: url(../images/background-line.png);
}

.login-window .z-window-content {
     background-image: url(../images/background-world-physical.png) !important;
     background-position: center;
	 /*background-attachment: scroll;*/
	 background-repeat: no-repeat;
}

.login-box-body {
	width: 660px;
    background: rgba(234,240,256,0.5);
	border-radius: 20px;
	font-weight: 100;
	color: #222222;
	z-index: 1;
	padding: 60px 40px 60px 40px;
	margin: 0;
	text-align: center;
/*
	background-image: url(../images/login-logo.png) !important;
	background-repeat: no-repeat;
*/
}

.login-box-header {
	<%-- background-image: url(../images/login-box-header.png); --%>
	background-color: transparent;
	z-index: 2;
	height: 54px;
	width: 660px;
}

.login-box-header-txt {
	color: transparent !important;
	font-weight: bold;
	position: relative;
	top: 30px;
}

.login-box-header-logo {
	padding-top: 20px;
	padding-bottom: 25px;
}

.login-box-footer {
	/*background-image: url(../images/login-box-footer.png);*/
	background-position: top right;
	background-attachment: scroll;
	background-repeat: repeat-y;
	z-index: 2;
	height: 110px;
	width: 660px;
}

.login-label .z-label {
    color: #222222;
}

.login-box-footer .confirm-panel {
	width: 600px !important;
}

.login-box-footer-pnl {
	width: 604px;
	margin-left: 10px;
	margin-right: 10px;
	padding-top: 40px !important;
}

.login-label {
	color: black;
	text-align: right;
	width: 40%;
}

.login-field {
	text-align: left;
	width: 55%;
}

.login-btn {
	padding: 4px 20px !important;
}

.login-east-panel, .login-west-panel {
	width: 350px;
	background-color: #E0EAF7;
	position: relative;
}
@media screen and (max-width: 659px) {
	.login-box-body, .login-box-header, .login-box-footer {
		background-image: none;
		width: 90%;
	}
	.login-box-footer .confirm-panel, .login-box-footer-pnl {
		width: 90% !important;
	}
	.login-box-header-txt {
		display: none;
	}
}
@media screen and (max-height: 600px) {
	.login-box-header-txt {
		display: none;
	}
	.login-box-body, .login-box-header, .login-box-footer {
		background-image: none;
	}
	.login-box-body {
		padding-bottom: 10px;
	}
	.login-box-header {
		height: 0px;
	}
}
@media screen and (max-width: 359px) {
	.login-window .z-center > .z-center-body .z-window.z-window-embedded > .z-window-content {
		padding: 0px
	}
}
