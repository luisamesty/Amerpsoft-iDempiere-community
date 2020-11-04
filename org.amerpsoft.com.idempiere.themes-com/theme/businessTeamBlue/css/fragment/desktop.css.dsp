<%-- header --%>
.desktop-header-left {
	margin: 0;
	margin-left: 1px;
	margin-top: 1px;
	background-color: transparent !important; 
	border: none !important;
}

.desktop-header-right {
	margin: 0;
	margin-top: 1px;
	padding-right:1px;
	background-color: transparent !important; 
	border: none !important;
}

.desktop-header {
    /* background-color: #E4E4E4; */
    background: -moz-linear-gradient(top, #244771 0%, #2893B7 80%);
    background: -webkit-linear-gradient(top, #244771 0%,#2893B7 80%);
    background: linear-gradient(to bottom, #D8E0EB 0%,#FEFEFF 80%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#728077', endColorstr='#ed434141',GradientType=0 );
    width: 100%;
    height: 46px;
    border-bottom: 3px solid #fcc654 !important;
}

/*EGS GROUP*/
.z-tabs-content {
    display: table;
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    border-bottom: 1px solid #FCC654 !important;
    margin: 5px 0 0 0;
    padding-left: 10px;
    padding-top: 0;
    list-style-image: none;
    list-style-position: outside;
    list-style-type: none;
    zoom: 1;
    clear: both;
}

td.z-listcell {
    border-bottom: 0.1em solid #CDD7BB;
    border-left: 0.1em solid #CDD7BB;
    /* border-left: none; */
}

.desktop-header-font > span {
    padding: 0px;
    color: #000 !important;
    text-shadow: none !important;
}

.desktop-header-username {
    color: #fff !important;
    text-decoration: none;
}

/*FIN EGS GROUP*/

}
.desktop-header.mobile {
	height: 36px;
	padding: 4px 4px;
}

.desktop-header-font {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
}

.desktop-header-popup {
	width: 800px;
	border-radius: 2px;
	border: 1px solid #d5d5d5;
	border-right: 2px solid #d5d5d5;
	border-bottom-width: 2px;
	right: 1px;
}

.desktop-header-popup .desktop-header {
	border: none;
	height: 100% !important;
}

.desktop-header-username {
	padding-right: 6px;
}

.desktop-header-username:hover {
	color: blue;
	text-decoration: underline;
}

.desktop-user-panel {
	float: right;
}
.desktop-user-panel.mobile .desktop-header-font.desktop-header-username {
	font-weight: 600;
}

.desktop-layout {
	position: absolute; 
	border: none;
	background-color: #E4E4E4;
}

.desktop-tabbox {
	padding-top: 0px; 
	background-color: #FAFAFC;
}

.desktop-tabbox .z-tabs-content {
    width: 5555px !important;
}

.desktop-tabbox .z-tab {
	height: 24px;
}
@media screen and (max-width: 360px) {
	.desktop-tabbox .z-tab {
		max-width: 190px;
	}
}

.desktop-tabbox .z-tab-selected {
	height: 25px;
}

 /*INICIO EGS GROUP*/
 
.desktop-tabbox .z-tab-selected {
	border-top: 2px solid #FCC654;
	border-top-left-radius: 5px 5px;
	border-top-right-radius: 5px 5px;
}

.desktop-tabbox .z-tab .z-tab-text {
	padding-top: 0px;
	padding-bottom: 0px;
}

.desktop-tabbox .z-tab-selected .z-tab-text {
	color: #fff;
    font-weight: 700;
    font-family: Helvetica,Arial,sans-serif;
    background-image: -moz-linear-gradient(top, #fff, #ddd);
    background-image: -ms-linear-gradient(top, #fff, #ddd);
    background-image: -o-linear-gradient(top, #fff, #ddd);
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    background: #7abcff;
    background: -moz-linear-gradient(top, #7abcff 0%, #60abf8 44%, #4096ee 100%);
    background: -webkit-linear-gradient(top, #7abcff 0%,#60abf8 44%,#4096ee 100%);
    background: linear-gradient(to bottom, #7abcff 30%,#60abf8 20%,#4096ee 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7abcff', endColorstr='#4096ee',GradientType=0 );
}

.desktop-tabbox .z-tab-selected .z-tab-button {
    color: #636363;
    width: 25px;
    height: 110%;
     background-image: -moz-linear-gradient(top, #fff, #ddd);
    background-image: -ms-linear-gradient(top, #fff, #ddd);
    background-image: -o-linear-gradient(top, #fff, #ddd);
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    background: #7abcff;
    background: -moz-linear-gradient(top, #7abcff 0%, #60abf8 44%, #4096ee 100%);
    background: -webkit-linear-gradient(top, #7abcff 0%,#60abf8 44%,#4096ee 100%);
    background: linear-gradient(to bottom, #7abcff 30%,#60abf8 20%,#4096ee 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7abcff', endColorstr='#4096ee',GradientType=0 );
}

.desktop-tabbox .z-tab {
    padding-top: 0px;
    background: #f0f9ff;
    background: -moz-linear-gradient(top, #f0f9ff 15%, #c3d4ea 30%, #9db7db 100%);
    background: -webkit-linear-gradient(top, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%);
    background: linear-gradient(to bottom, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%)
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f0f9ff', endColorstr='#9db7db',GradientType=0 );
    border: solid 1px #9AADC5;
}

.desktop-tabbox .z-tab-selected::before {
    content: '';
    position: absolute;
    z-index: 1;
    top: -.5px;
    left: -.5em;
    bottom: -2px;
    width: 1em;
    background-image: -moz-linear-gradient(top, #fff, #ddd);
    background-image: -ms-linear-gradient(top, #fff, #ddd);
    background-image: -o-linear-gradient(top, #fff, #ddd);
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    -webkit-transform: skew(10deg);
    -moz-transform: skew(10deg);
    -ms-transform: skew(10deg);
    -o-transform: skew(10deg);
    transform: skew(-20deg);
    -webkit-border-radius: 0 5px 0 0;
    -moz-border-radius: 0 5px 0 0;
    border-radius: 0 5px 0 0;
    background: #7abcff;
    background: -moz-linear-gradient(top, #7abcff 0%, #60abf8 44%, #4096ee 100%);
    background: -webkit-linear-gradient(top, #7abcff 0%,#60abf8 44%,#4096ee 100%);
    background: linear-gradient(to bottom, #7abcff 30%,#60abf8 20%,#4096ee 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7abcff', endColorstr='#4096ee',GradientType=0 );
}

 /* FIN EGS GROUP*/
.desktop-north, .desktop-center {
	border: none;
}

.desktop-center {
	padding-top: 1px;
	background-color: #E4E4E4;
}

.desktop-tabpanel {
	margin: 0;
	padding: 0;
	border: 0;
	position: relative !important;
	background-color: #FFFFFF
}

.desktop-left-column {
	width: 200px;
	border: none;
	border-right: 1px solid #C5C5C5;
	background-color: #E4E4E4;
	padding-top: 2px; 
}

.desktop-right-column {
	width: 200px;
	border: none;
	border-left: 1px solid #C5C5C5;
	background-color: #E4E4E4;
	padding-top: 2px; 
}

.desktop-left-column + .z-west-splitter,  .desktop-left-column.z-west {
	border-top: none; 
	border-right: 1px solid #c5c5c5;
}

.desktop-right-column + .z-east-splitter,  .desktop-right-column.z-east {
	border-top: none; 
	border-left: 1px solid #c5c5c5;
}

.desktop-left-column .z-west-body {
	border-right: none;
}

.desktop-right-column .z-east-body {
	border-left: none;
}

.desktop-layout > div > .z-west-collapsed, .desktop-layout > div > .z-east-collapsed {
	border-top: none;
}

.desktop-left-column .z-anchorlayout, .desktop-right-column .z-anchorlayout {
	overflow-x: hidden;
}

.z-anchorlayout { overflow:auto }
 
.z-anchorchildren { overflow:visible }

.desktop-hometab {
	margin-left: 2px !important;
}

.desktop-tabbox .z-tabs .z-toolbar-tabs-body {
	padding: 0px !important;
	margin: 0px !important;
}

.desktop-tabbox .z-tabs .z-toolbar-tabs-body .z-toolbarbutton {
	padding: 0px !important;
	border: 1px solid transparent !important;
	margin: 0px !important;
}

.desktop-tabbox .z-tabs .z-toolbar-tabs .z-toolbarbutton-hover {
	border: none !important;
	padding: 0px !important;
	margin: 0px !important;
}

.desktop-tabbox .z-tabs .z-toolbar-tabs .z-toolbarbutton-hover .z-toolbarbutton-content {
	background-image: none !important;
	background-color:#DDD !important;
	padding: 0px !important;
	margin: 0px !important;
	-webkit-box-shadow:inset 0px 0px 3px #CCC;
	-moz-box-shadow:inset 0px 0px 3px #CCC;	
	-o-box-shadow:inset 0px 0px 3px #CCC;	
	-ms-box-shadow:inset 0px 0px 3px #CCC;	
	box-shadow:inset 0px 0px 3px #CCC;
}

.desktop-menu-popup {
	z-index: 9999;
	background-color: #fff;
}

.desktop-menu-toolbar {
	background-color: #ffffff; 
	verticle-align: middle; 
	padding: 2px;
	border-top: 1px solid #c5c5c5;
}

.desktop-home-tabpanel {
	background-color: #FFFFFF;
	width: 100% !important;
}

.link {
	cursor:pointer;
	padding: 2px 2px 4px 4px;
	border: none !important;
}

.link.z-toolbarbutton:hover {
	border: none !important;
	background-image: none !important;
	text-decoration: underline;
}

.link.z-toolbarbutton:hover span {
	color: blue;
}

.desktop-home-tabpanel .z-panel-head {
	background-color: #FFFFFF;
}

<%-- window container --%>
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content img {
	width: 22px;
	height: 22px;
	padding: 3px 3px;
}
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content [class^="z-icon"] {
	width: 22px;
	height: 22px;
	padding: 3px 3px;
}
.window-container-toolbar > .z-toolbar-content,
.window-container-toolbar-btn.z-toolbarbutton, 
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content,
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content [class^="z-icon"] {
	display:inline-flex;
	align-items: center;
	justify-content: center; 
	border-bottom: 0px; 
}

.user-panel-popup .z-popup-content {
	padding-left: 0px;
	padding-right: 0px;
}
.user-panel-popup .z-popup-content > .z-vlayout {
	overflow-x: auto;
	padding: 8px;
}
.window-container-toolbar-btn.context-help-btn .z-toolbarbutton-content img {
	width: 16px;
	height: 16px;
	padding: 3px 3px;
}
