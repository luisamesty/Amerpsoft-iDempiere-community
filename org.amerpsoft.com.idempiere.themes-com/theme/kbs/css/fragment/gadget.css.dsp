.fav-new-btn {
	margin-left: 4px;
	margin-bottom: 3px;
	padding-left: 1px;
}

.fav-new-btn img {
	height: 16px;
	width: 16px;
}

.z-panel-head {
	background-image: none;
	background-color: #91BEE6;
}

.z-panel {
	border-radius: 5px;
	border: 1px solid #d8d8d8;
	border-bottom-width: 1px;
}

.z-panel-noborder {
	border: none !important;
}

.z-panelchildren {
	border: none;
}

.z-panel-head {
	padding-bottom: 1px;
	border: none;
}

.z-panel-head .z-panel-header {
	padding: 0 0 2px 0;
	color: #262626;
	background: #91BEE6;
	font-weight: 300;
	font-size: 13px;
}	

.z-caption .z-caption-content {
	padding: 1px 1px 1px 6px;
	color: #262626; 
	font-weight: 300;
	font-size: 13px;
	cursor: move;
}

.dashboard-layout {
	width: 100%;
	height: 100%;
	position: relative;
}

.dashboard-widget {
	margin-top: 4px; 
	margin-left: auto; 
	margin-right: auto;
	position: relative;
	width: 94%;	
}

.dashboard-widget-max {
	margin: auto;
	width: auto;	
}

.dashboard-widget.dashboard-widget-max > .z-panel-body > .z-panelchildren {
	overflow: auto;
}

.dashboard-report-iframe {
	min-height:300px; 
	border: 1px solid lightgray; 
	margin:auto;
	width: 99%;
	height: 90%;
}

.favourites-box {
	width: 90%;
	margin: auto;
	padding: 5px 0px 5px 0px;
}

.favourites-box .z-vbox {
	width: 100%;
}

.favourites-box .z-hbox {
	padding: 2px 0px;
	width: 100%;
}

.favourites-box .z-toolbar-start {
	float: right;
}
.favourites-box .trash-font-icon {
	font-family: FontAwesome;
	font-size: 20px;
}

.recentitems-box {
	width: 90%;
	margin: auto;
	padding: 5px 0px 5px 0px;
}

.recentitems-box A {
	display: block;
	padding: 2px 0px;
}

.recentitems-box .z-toolbar-start {
	float: right;
}
.recentitems-box A.trash-toolbarbutton {
	display: inline-block;
}
.recentitems-box .z-toolbar .z-toolbar-content {
	display: inline-flex;
	align-items: center;
}
.recentitems-box .trash-toolbarbutton .z-toolbarbutton-content {
	font-size: 16px;
}
.recentitems-box .trash-font-icon {
	font-family: FontAwesome;
	font-size: 20px;
}
	
.views-box {
	width: 95%;
	margin: auto;
	padding: 10px 0px 10px 0px !important;
}

.views-box .z-vbox {
	width: 99%;
}

.views-box .z-window-content .z-vbox .z-vbox-separator td {
    background-color: #FFFFFF;
}

.views-box .z-toolbarbutton {
	width: 100%;
    padding: 4px 4px 4px 4px;
    background: #FCF9EA !important;
    display: block;
    border: 1px solid #cccccc !important;
}

.views-box .z-toolbarbutton:hover {
	background: #FBD87E !important;
	text-decoration: none !important;
	display: block;
    border: 1px solid #cccccc !important;
	padding: 4px 4px 4px 4px;
}

.views-box .link img {
    height: 16px;
    width: 16px;
    border: 1px solid #E8E8E8;
    padding: 5px 5px 5px 5px;
    background: #E8E8E8;
}
.views-box .z-toolbarbutton [class^="z-icon"] {
	text-align: center;
}
.views-box .z-toolbarbutton [class^="z-icon"]:before {
	width: 14px;
}
.views-box .z-toolbarbutton-content{
	text-decoration: none !important;
	color: #333 !important;
}

.views-box .z-toolbarbutton-cnt {
	padding: 0px 0px 0px 0px;
	font-size: 11px !important;
}

.activities-box {
	width: 100%;
	margin: auto;
	padding: 5px 5px 5px 5px;
	cursor: pointer;
}

.activities-box .z-vbox {
	width: 100%;
}

.activities-box .z-button {
	text-align: left;
}

.recentitems-box .z-toolbar, .favourites-box .z-toolbar {
	margin-top: 5px;
	margin-bottom: 5px;
}

<%-- performance indicator --%>
.performance-indicator {
	margin: 0px; 
	position: relative; 
}
.performance-indicator img {
	display: block; 
	margin: 0px;
	padding:0px;
}
.window-view-pi .performance-indicator img {
}
.performance-indicator-box {
	background-color: #FCF9EA; 
	border: 1px solid #d8d8d8; 
	border-radius: 5px; 
	cursor: pointer;
}
.performance-indicator-title {
	text-align: center; 
	background-color: #c8c8c8; 
	border: 1px solid #c8c8c8;
	padding-top: 2px; 
	padding-bottom: 2px;
	line-height:12px;
}
.performance-panel .z-grid {
	border: none;
	margin:0px; 
	padding:0px; 
	position: relative;
	width: 100%;
}

.dashboard-widget.dashboard-widget-max .chart-gadget {
	height: 100% !important; 
}

.help-content
{
	padding: 5px;
	font-size: 11px;
	font-weight: normal;
}

.fav-new-btn.z-toolbarbutton [class^="z-icon-"] {
	font-size: smaller;
	color: #333;
	padding-left: 4px;
	padding-right: 4px;
}
