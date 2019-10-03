<%-- login --%>
.login-window {
	background-color: #E5E5E5;
}

.login-window .z-window-content {
	background-color: #E5E5E5;
}

.login-box-body {
	width: 1px;
   background: rgba(0,0,0,0.3);
	border-radius: 10px;
	font-weight: 100;
	color: white;
	z-index: 1;
	padding: 20px 40px 100px 40px;
	margin: 0;
	text-align: center;
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
<%-- 	padding-top: 20px;
	padding-bottom: 25px;--%>
/*	position: fixed;
	top: 80px;
	padding-left: 50px !important;*/
	padding-top: 20px;
	padding-bottom: 25px;
}

.login-box-footer {
	<%-- background-image: url(../images/login-box-footer.png); --%>
	background-position: top right;
	background-attachment: scroll;
	background-repeat: repeat-y;
	z-index: 2;
	height: 110px;
	width: 660px;
}

.login-label .z-label {
    color: #fff;
}

.login-box-footer .confirm-panel {
	width: 600px !important;
}

.login-box-footer-pnl {
	width: 604px;
	margin-left: 10px;
	margin-right: 10px;
	/* padding-top: 40px !important; */
	 margin-top: -70px;
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

<%-- Seccion Video EGS GROUP--%>
.ui--video-background .ui--gradient {
  background-color: #e1e1e1;
  background-image: -moz-linear-gradient(top, #ffffff, #e1e1e1);
  background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#e1e1e1));
  background-image: -webkit-linear-gradient(top, #ffffff, #e1e1e1);
  background-image: -o-linear-gradient(top, #ffffff, #e1e1e1);
  background-image: linear-gradient(to bottom, #ffffff, #e1e1e1);
  filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr='#ffffff', endColorstr='#e1e1e1');
  -ms-filter: "progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr='#ffffff', endColorstr='#e1e1e1')";
  background-repeat: repeat-x;
}

.ui--video-background-video {
  z-index: -1000;
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  overflow: hidden;
  opacity: 0.98; 
}

.ui--video-background .ui--gradient {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
}

.ui--video-background {
  z-index: -100;
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  opacity: .75;
}

video {
    position: fixed;
    top: 50%;
    left: 50%;
    min-width: 100%;
    min-height: 100%;
    width: auto;
    height: auto;
    z-index: -100;
    transform: translateX(-50%) translateY(-50%);
    background: url('../images/idempieregroup.jpg') no-repeat;
    background-size: cover;
    transition: 1s opacity;
    
}

<%-- Limpia fondo en caja login --%>
.login-window .z-window-content, .login-window .z-window {
	background: transparent;
}

.primary-login {
    background-color: #FFB94B;
    background-image: -moz-linear-gradient(center top , #FDDB6F, #FFB94B);
    border-radius: 3px;
    text-shadow: 0px 1px 0px rgba(255, 255, 255, 0.5);
    box-shadow: 0px 0px 1px rgba(0, 0, 0, 0.3), 0px 1px 0px rgba(255, 255, 255, 0.3) inset;
    border-width: 1px;
    border-style: solid;
    border-color: #D69E31 #E3A037 #D5982D;
    float: left;
    height: 35px;
    padding: 0px;
    width: 120px;
    cursor: pointer;
    font: bold 15px Arial,Helvetica;
    color: #8F5A0A;
}

.primary-login:hover,.primary-login:focus
{		
    background-color: #fddb6f;
    background-image: -webkit-gradient(linear, left top, left bottom, from(#ffb94b), to(#fddb6f));
    background-image: -webkit-linear-gradient(top, #ffb94b, #fddb6f);
    background-image: -moz-linear-gradient(top, #ffb94b, #fddb6f);
    background-image: -ms-linear-gradient(top, #ffb94b, #fddb6f);
    background-image: -o-linear-gradient(top, #ffb94b, #fddb6f);
    background-image: linear-gradient(top, #ffb94b, #fddb6f);
}	

.primary-login:active
{		
    outline: none;
   
     -moz-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) inset;
     -webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) inset;
     box-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) inset;		
}

.primary-login::-moz-focus-inner
{
  border: none;
}


.modern-input
{
    padding: 10px 10px 10px 30px;
    margin: 0 0 10px 0;
    font-size: 90%;
    width: 353px;
    border: 1px solid #ccc;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-box-shadow: 0 1px 1px #ccc inset, 0 1px 0 #fff;
    -webkit-box-shadow: 0 1px 1px #ccc inset, 0 1px 0 #fff;
    box-shadow: 0 1px 1px #ccc inset, 0 1px 0 #fff;
    background-position: 6px -5px !important;
}

.modern-combobox {
	 background: #f1f1f1 url(../images/login-user.png) no-repeat;
	 width: 190px !important;
}

.modern-combobox:focus {
    background: #FDD676 url(../images/login-user.png) no-repeat !important;
    background-position: 6px -5px !important;
}

.modern-user{
   background: #f1f1f1 url(../images/login-user.png) no-repeat;
}

.modern-user:focus {
    background: #FDD676 url(../images/login-user.png) no-repeat !important;
    background-position: 6px -5px !important;
}

.modern-password {
   background: #f1f1f1 url(../images/login-pass.png) no-repeat;
}

.modern-password:focus {
    background: #FDD676 url(../images/login-pass.png) no-repeat;
    background-position: 6px -5px !important;
}


.modern-label {
    color: white !important;
    font-size: 14px !important;
}

.modern-label-2 {
    font-size: 13px !important;
}
.modern-checkbox-content {
 	font-size: 14px !important;
}

.modern-combobox-button {
  	min-height: 40px !important;
	font-size: 20px !important;
	height: 40px !important;
	margin-top: 9px !important;
	line-height: 20px !important;
	padding: 9px 7px 5px 10px !important;
	color: #636363;
	border: 1px solid #CFCFCF;
	background: #FFF none repeat scroll 0% 0%;
	cursor: pointer;
}

.login-box-rol {
    width: 1px;
    background: rgba(0,0,0,0.3);
    border-radius: 10px;
    font-weight: 100;
    color: white;
    z-index: 1;
    padding: 20px 100px 100px 40px;
    margin: 0;
    text-align: center;
}

.modern-combo-item {
    width: 360px important;
    display: block;
    cursor: pointer;
    position: relative;
    text-shadow: 0 1px #fff;
    padding: 10px 5px 10px 30px;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
}

.modern-combobox-popup {
    width: 232px !important;
}

.box-help {
	display: block;
	cursor: pointer;
	border: medium none;
	padding: 0.4rem;
	background: rgba(255, 255, 255, 0.32) none repeat scroll 0% 0%;
	color: #FFF;
	border-radius: 3px;
	width: 124px;
	font-size: 0.9rem;
	transition: background 0.3s ease 0s;
	text-shadow: 0px 1px 0px rgba(0, 0, 0, 0.8);
	//margin: 1rem auto;
	font-weight: 700;
	box-shadow: 0px 0px 1px rgba(0, 0, 0, 0.3), 0px 1px 0px rgba(255, 255, 255, 0.3) inset;
	border-width: 1px;
	border-style: solid;
	border-color: #EEEEEE #DDDDDD #CCCCCC;
}

.box-help-errase {
    margin: 0 0 0 3px;
    padding: 5px 10px;
    font-size: 13px;
    font-weight: bold;
    border: 0;
    background: #eee;
    -moz-border-radius: 0 5px 5px 0;
    -webkit-border-radius: 0 5px 5px 0;
    border-radius: 0 5px 5px 0;
    -moz-box-shadow: 0 1px 0 rgba(0, 0, 0, 0.6);
    -webkit-box-shadow: 0 1px 0 rgba(0, 0, 0, 0.6);
    box-shadow: 0 1px 0 rgba(0, 0, 0, 0.6);
    background-color: #444;
    background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, color-stop(0%, #989898), color-stop(10%, #6a6a6a), color-stop(50%, #3c3c3c), color-stop(50%, #353535), color-stop(100%, #4e4e4e));
    background-image: -moz-linear-gradient(top, #989898 0%, #6a6a6a 10%, #3c3c3c 50%, #353535 50%, #4e4e4e 100%);
    background-image: linear-gradient(top, #989898 0%, #6a6a6a 10%, #3c3c3c 50%, #353535 50%, #4e4e4e 100%);
    color: #fff;
    float: left;
    font-family: bold 15px Arial,Helvetica;
    padding: 8px 2px;
    text-decoration: none;
    width: 124px;
    text-align: center;
}
