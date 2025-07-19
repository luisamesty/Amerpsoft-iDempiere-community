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
	background-color: #E4E4E4;
	width: 100%;
	height: 46px;
	border-bottom: 1px solid #C5C5C5 !important;
	padding-left: 4px;
}
.desktop-header.mobile {
	height: 36px;
	padding: 4px 4px;
}

.desktop-header-font {
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
}

.desktop-tabbox {
	padding-top: 0px; 
}

.desktop-tabbox .z-tabs-content {
    width: 5555px !important;
}

.desktop-tabbox .z-tab {
}
@media screen and (max-width: 360px) {
	.desktop-tabbox .z-tab {
		max-width: 190px;
	}
}

.desktop-tabbox .z-tab-selected {
}

.desktop-tabbox .z-tab-selected {
}

.desktop-tabbox .z-tab .z-tab-text {
	padding-top: 0px;
	padding-bottom: 0px;
}

.desktop-tabbox > .z-tabpanels {
	flex-grow: 1 1 0;
}

.desktop-north, .desktop-center {
	border: none;
}

.desktop-center {
	padding-top: 1px;
}

.desktop-tabpanel {
	margin: 0;
	padding: 0;
	border: 0;
	position: relative !important;
}

.desktop-left-column {
	width: 200px;
	border: none;
	border-right: 1px solid #C5C5C5;
	background-color: #e9ebee;
	padding-top: 2px; 
}

.desktop-right-column {
	width: 200px;
	border: none;
	border-left: 1px solid #C5C5C5;
	background-color: #e9ebee;
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
}

.desktop-menu-toolbar {
	verticle-align: middle; 
	padding: 2px;
	border-top: 1px solid #c5c5c5;
}

.desktop-home-tabpanel {
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
}

<%-- window container --%>
.window-container-toolbar-btn.z-toolbarbutton {
	width: 28px;
}
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content img {
	width: 22px;
	height: 22px;
	padding: 3px 3px;
}
.window-container-toolbar-btn.z-toolbarbutton .z-toolbarbutton-content [class^="z-icon"] {
	padding: 3px 3px;
	font-size: 22px;
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
