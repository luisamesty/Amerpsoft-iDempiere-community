<%-- Attachment Item --%>
.z-attachment-item {
	border: 1px solid #dcdcdc;
	border-radius: 4px;
	background-color: #f5f5f5;
	width: auto !important;
	display: inline-block;
	margin-right: 5px; 
	margin-bottom: 5px;
	padding-left: 5px;
	padding-right: 5px;
}

.z-attachment-item-del-button {
	float: right;
	background-color: #f5f5f5;
}

<%-- Combobox --%>
.z-combobox-disabled, .z-combobox[disabled] {
	color: rgba(0,0,0,0.7) !important; 
	cursor: default !important; 
}

.z-combobox-disabled > input {
	color: rgba(0,0,0,0.7) !important; 
	cursor: default !important;
}

.z-combobox-text-disabled {
	background-color: #ECEAE4 !important;
}
.z-comboitem {
	min-height:14px;
}
<%-- highlight focus form element --%>
input:focus, textarea:focus, .z-combobox-input:focus, z-datebox-input:focus, select:focus {
	border: 1px solid #0000ff;
	background: #FFFFCC;
}

.z-textbox-readonly, .z-intbox-readonly, .z-longbox-readonly, .z-doublebox-readonly, 
.z-decimalbox-readonly, .z-datebox-readonly, .z-timebox-readonly, .editor-input-disd, 
.z-textbox[readonly], .z-intbox[readonly], .z-longbox[readonly], .z-doublebox[readonly], 
.z-decimalbox[readonly], .z-datebox[readonly], .z-timebox[readonly] {
	background-color: #F0F0F0;
}

.z-textbox[disabled], .z-intbox[disabled], .z-longbox[disabled], .z-doublebox[disabled], 
.z-decimalbox[disabled], .z-datebox[disabled], .z-timebox[disabled] {
	color: black !important;
	background-color: #F0F0F0 !important;
	cursor: default !important;
	opacity: 1 !important;
	border: 1px solid #ECECEC;
}

.z-textbox, .z-decimalbox, .z-intbox, .z-longbox, .z-doublebox,
.z-datebox-input, .z-datebox-button, .z-timebox-input, .z-timebox-button,
.z-combobox-input, .z-combobox-button {
	border: 1px solid #ECECEC;
}

<%-- workaround for http://jira.idempiere.com/browse/IDEMPIERE-692 --%>
.z-combobox-popup {
	max-height: 200px;
}
