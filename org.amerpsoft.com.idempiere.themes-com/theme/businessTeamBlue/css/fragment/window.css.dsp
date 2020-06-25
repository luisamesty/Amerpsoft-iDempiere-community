div.wc-modal, div.wc-modal-none, div.wc-highlighted, div.wc-highlighted-none {
	background-color: white;
}

.z-window-embedded .z-window-content {
	border: none;
}

.z-window-embedded .z-window-header, 
.z-window-embedded .z-window-content {
	background-image: none;
}

.z-modal-mask {
	z-index: 1800 !important;
}
.z-window {
	padding: 0px;
}

.z-window-embedded .z-window-content {
	border: none;
}

.z-window-overlapped .z-window-content,  .z-window-modal .z-window-content, 
.z-window-highlighted .z-window-content, .z-window-embedded .z-window-content {
	border: none;
}

.z-window-header {
    padding: 4px;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    background-color: #A1A0A0;
    border-color: #484848;
    border-bottom: solid 3px #FCC654;
}
.z-window-embedded > .z-window-header {
	border-radius: 0px;
	background-color: transparent;
}

.z-window-overlapped, .z-window-popup, .z-window-modal, .z-window-highlighted, 
.embedded-dialog .z-window-embedded
{
	background-color: #fff;
	margin: 0px;
}

.z-window-overlapped .z-window-header,
.z-window-popup .z-window-header,
.z-window-modal .z-window-header,
.z-window-highlighted .z-window-header
{
	color: #fff;
	font-weight: bold;
}

.z-window-header, .z-window-content {
	background-image: none !important;
}

.z-window-modal-shadow, .z-window-overlapped-shadow, .z-window-popup-shadow, .z-window-embedded-shadow, .z-window-highlighted-shadow
{
	border-radius: 0px !important;
}

<%-- dialog --%>
.embedded-dialog {
	position: absolute;
}

.embedded-dialog .z-window-embedded-header {
	color: #fff;
	font-weight: bold;
}

.popup-dialog .z-window-overlapped .z-window-content, .popup-dialog .z-window-highlighted .z-window-content {
	padding: 0px;
	background-color: #f5f5f5;
}
.popup-dialog > .z-window-content, .info-panel > .z-window-content {
	padding: 0px;
	border-bottom-left-radius: 4px;
	border-bottom-right-radius: 4px;
}
.popup-dialog .dialog-content {
	padding: 0px 8px !important;
	--margin-bottom: 20px !important;
}

.popup-dialog .z-window-header {
    padding: 4px;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    /* background-color: #A1A0A0; */
    border-color: #484848;
    border-bottom: solid 3px #FCC654;
    background-image: -moz-linear-gradient(top, #fff, #ddd);
    background-image: -ms-linear-gradient(top, #fff, #ddd);
    background-image: -o-linear-gradient(top, #fff, #ddd);
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    background: #7abcff !important;
    background: -moz-linear-gradient(top, #7abcff 0%, #60abf8 44%, #4096ee 100%) !important;
    background: -webkit-linear-gradient(top, #7abcff 0%,#60abf8 44%,#4096ee 100%) !important;
    background: linear-gradient(to bottom, #7FA1DD 3%,#3B5EAF 50%,#28478E 100%) !important;
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7abcff', endColorstr='#4096ee',GradientType=0) !important;
}

.popup-dialog .z-window-icon {
    color: #AFC8FD;
    display: block;
    border: 1px solid #0D1E44;
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -o-border-radius: 4px;
    -ms-border-radius: 4px;
    border-radius: 4px;
    margin: auto 1px;
    background: #fefefe;
    background: -webkit-gradient(linear,left top,left bottom,color-stop(0%,#fefefe),color-stop(100%,#eee));
    background: -webkit-linear-gradient(top,#fefefe 0,#eee 100%);
    background: -o-linear-gradient(top,#fefefe 0,#eee 100%);
    background: -ms-linear-gradient(top,#fefefe 0,#eee 100%);
    background: linear-gradient(to bottom,#3F60AB 0,#3F60AB 100%);
    text-align: center;
    overflow: hidden;
    cursor: pointer;
    float: right;
}

.popup-dialog.z-window-overlapped .dialog-footer {
	padding: 12px 15px 8px 15px !important;
}

.popup-dialog.z-window-highlighted .dialog-footer {
	padding: 12px 15px 8px 15px !important;
}

.dialog-footer {
	margin-bottom: 0;
	background-color: #f5f5f5;
	border-top: 1px solid #ddd;
	-webkit-box-shadow: inset 0 1px 0 #ffffff;
	-moz-box-shadow: inset 0 1px 0 #ffffff;
	box-shadow: inset 0 1px 0 #ffffff;
}

.btn-ok {
	font-weight: bold;
}

<%-- notification message --%>
.z-notification .z-notification-content {
    width: 400px;
}

.z-notification {
	padding: 3px !important;
}

input[type="checkbox"]:focus
{
 	 outline: #0000ff auto 1px;
	-moz-outline-radius: 3px;
}

<%-- Quick Form Read-only Component --%>
.quickform-readonly .z-textbox-readonly, .quickform-readonly .z-intbox-readonly, .quickform-readonly .z-longbox-readonly, .quickform-readonly .z-doublebox-readonly,
.quickform-readonly .z-decimalbox-readonly, .quickform-readonly .z-datebox-readonly, .quickform-readonly .z-timebox-readonly, .quickform-readonly .editor-input-disd,
.quickform-readonly .z-textbox[readonly], .quickform-readonly .z-intbox[readonly], .quickform-readonly .z-longbox[readonly], .quickform-readonly .z-doublebox[readonly],
.quickform-readonly .z-decimalbox[readonly], .quickform-readonly .z-datebox[readonly], .quickform-readonly .z-timebox[readonly]
{
    color: #252525 !important;
    opacity: .8;
}