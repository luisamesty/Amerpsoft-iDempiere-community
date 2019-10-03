.mandatory-decorator-text {
	text-decoration: none; font-size: xx-small; vertical-align: top; color:red;
}

.editor-box {
	display: inline-block;
	border: none; 
	padding: 0px; 
	margin: 0px; 
	background-color: transparent;
	position: relative;
}

.editor-input {
	box-sizing: border-box;
	-moz-box-sizing: border-box; /* Firefox */
	display: inline-block;
	padding-right: 22px; 
	width: 100%;
	height: 21px;
}

.editor-input:focus {
	border: 1px solid #0000ff;
}

.editor-input-disd {
	padding-right: 0px !important;
}
	
.editor-button {
	padding: 0px;
	margin: 0px;
	display: inline-block;
	background-color: transparent;
	background-image: none;
	width: 20px;
	height: 22px;
	min-height: 22px;
	border: none;
	position: absolute;
	right: 1px;
	top: 2px;
}

.editor-button :hover {
	-webkit-filter: contrast(1.5);
	filter: contrast(150%);
}

.editor-button img {
	vertical-align: top;
	text-align: left;
	width: 18px;
	height: 18px;
	padding: 1px 1px;
}


.editor-box .grid-editor-input.z-textbox {
}

.grid-editor-button {
}

.grid-editor-button img {
}

.number-box {
	display: inline-block; 
	white-space:nowrap;
}

.number-box .grid-editor-input.z-decimalbox {
}

.datetime-box {
	display: inline-block;
	white-space:nowrap;
}

.datetime-box .z-datebox {
	display: inline;
}

.datetime-box .z-timebox {
	display: inline;
}

span.grid-combobox-editor {
	width: 100% !important;
	position: relative;
}

.grid-combobox-editor input {
	width: 100% !important;
	padding-right: 26px;
	border-bottom-right-radius: 6px;
	border-top-right-radius: 6px;
	border-right: 0px;
}

.grid-combobox-editor.z-combobox-disabled input {
	border-bottom-right-radius: 3px;
	border-top-right-radius: 3px;
	border-right: 1px solid #cfcfcf;
	padding-right: 5px;
}

.grid-combobox-editor .z-combobox-button {
	position: absolute;
	right: 0px;
	top: 1px;
	border-bottom-right-radius: 3px;
	border-top-right-radius: 3px;
	border-bottom-left-radius: 0px;
	border-top-left-radius: 0px;
}

.grid-combobox-editor input:focus {
	border-right: 0px;
}
	
.grid-combobox-editor input:focus + .z-combobox-button {
	border-left: 1px solid #0000ff;
}

<%-- payment rule --%>
.payment-rule-editor {
	display: inline-block;
	border: none; 
	padding: 0px; 
	margin: 0px; 
	background-color: transparent;
	position: relative;
}
.payment-rule-editor .z-combobox {
	width: 100%;
}
.payment-rule-editor .z-combobox-input {
	display: inline-block;
	padding-right: 44px; 
	width: 100%;
	height: 24px;
	border-bottom-right-radius: 6px;
	border-top-right-radius: 6px;
	border-right: 0px;
}
.payment-rule-editor .z-combobox-input:focus {
	border: 1px solid #0000ff;
}
.payment-rule-editor .z-combobox-input.editor-input-disd {
	padding-right: 22px !important;
}
.payment-rule-editor .z-combobox-button {
	position: absolute;
	right: 0px;
	top: 1px;
}
.payment-rule-editor .z-combobox .z-combobox-button-hover {
	background-color: #ddd;
	background-position: 0px 0px;
}
.payment-rule-editor .editor-button {
	border-radius: 0px;
	right: 24px;
}

<%-- chart --%>
.chart-field {
	padding: 10px; 
	border: 1px solid lightgray !important;
}

.field-label {
	position: relative; 
	float: right;
}

<%-- EGS GROUP --%>

.z-editorbox-egs {
   background: #F8FFD6 ;
}
.egs_textbox_mandatory.z-combobox input {
   background: #FEFDE1; 
   color: #000000;
   border: 1px solid #C7AA64 !important;
}

.egs_textbox_mandatory.z-combobox input:hover {
   /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#ffe9a9+0,fed97a+100 */
background: rgb(255,233,169); /* Old browsers */
background: -moz-linear-gradient(top,  rgba(255,233,169,1) 0%, rgba(254,217,122,1) 100%); /* FF3.6-15 */
background: -webkit-linear-gradient(top,  rgba(255,233,169,1) 0%,rgba(254,217,122,1) 100%); /* Chrome10-25,Safari5.1-6 */
background: linear-gradient(to bottom,  rgba(255,233,169,1) 0%,rgba(254,217,122,1) 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffe9a9', endColorstr='#fed97a',GradientType=0 ); /* IE6-9 */

   color: #000000;
   border: 1px solid #C7AA64 !important;
}

.adwindow-breadcrumb {
    height: 30px;
    padding: 0px;
    background: rgb(183,183,183);
    background: -moz-linear-gradient(top, rgba(183,183,183,1) 1%, rgba(154,153,156,1) 100%);
    background: -webkit-linear-gradient(top, rgba(183,183,183,1) 1%,rgba(154,153,156,1) 100%);
    background: linear-gradient(to bottom, rgba(183,183,183,1) 1%,rgba(154,153,156,1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#b7b7b7', endColorstr='#9a999c',GradientType=0 );
    padding-left: 5px;
    border-bottom: 4px solid #F0D284!important;
    clear: both;
}

input, textarea {
    border: solid 1px #000 !important;
}


.z-datebox-egs .z-datebox-inp {
  background-color: #F8FFD6;
  border: 1px solid #EEBC3F;
}
