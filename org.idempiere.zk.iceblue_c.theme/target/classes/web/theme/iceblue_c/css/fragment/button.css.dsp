.z-button {
  display: inline-block;
  padding: 4px 20px;
  text-align: center;
  cursor: pointer;
}

.z-button-hover,
.z-button-focus,
.z-button-click,
.z-button-disabled {
  color: #333333;
  background-color: #dde4e6 !important;
}

.z-button-click {
  background-color: #cccccc \9;
}

.z-button-hover,
.z-button-focus {
  border-color: transparent !important;
  color: #333333;
  text-decoration: none;
  background-position: 0 -15px;
  -webkit-transition: background-position 0.1s linear;
     -moz-transition: background-position 0.1s linear;
       -o-transition: background-position 0.1s linear;
          transition: background-position 0.1s linear;
}

.z-button-focus {
  outline: 5px auto -webkit-focus-ring-color;
}

.z-button-click {
  background-image: none;
  outline: 0;
  -webkit-box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
     -moz-box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
          box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15), 0 1px 2px rgba(0, 0, 0, 0.05);
}

.z-button-disabled {
  cursor: default;
  background-image: none;
  opacity: 0.65;
  filter: alpha(opacity=65);
  -webkit-box-shadow: none;
     -moz-box-shadow: none;
          box-shadow: none;
}

.z-button.btn-small {
	padding: 1px 5px;
}
.z-button.btn-medium {
	padding: 2px 10px;
}

.z-button-disabled {
	color: black; cursor: default; opacity: .6; -moz-opacity: .6; -khtml-opacity: .6; filter: alpha(opacity=60);
}

.img-btn img {
	height: 22px;
	width: 22px;
	background-color: transparent;
}

.txt-btn img, .small-img-btn img {
	height: 16px;
	width: 16px;
	background-color: transparent;
	vertical-align: middle;
	display: inline-block;
}

.btn-sorttab {
	box-shadow: 0px 0px 4px #bbb;
	color: #555;
	border: solid 1px #bbb;
	text-shadow: 0px 1px 2px #888;
}

.z-button [class^="z-icon-"][disabled],
.z-button-os [class^="z-icon-"][disabled]{
	font-size: larger;
	color: #333;	
	padding-left: 2px;
	padding-right: 2px;
}
.z-button.xlarge-toolbarbutton [class^="z-icon-"] {
	font-size: 24px;
}
.z-button.large-toolbarbutton [class^="z-icon-"] {
	font-size: 20px;
}
.z-button.medium-toolbarbutton [class^="z-icon-"] {
	font-size: 16px;
}
.z-button.small-toolbarbutton [class^="z-icon-"] {
	font-size: 12px;
}
.btn-ok, .btn-ok:focus {
	background-color: green;
}
.btn-cancel, .btn-cancel:focus {
	background-color: red;
}
.btn-ok.z-button [class^="z-icon-"]:before {
	color: white;	
}
.btn-cancel.z-button [class^="z-icon-"]:before {
	color: white;	
}
.z-datebox-button {
	background-color: #0093f9 !important;
	color: white !important;
}

.btn-negate.z-button {
	background: none;
	border: none;
	margin: 0px !important;
	padding: 0px;
	min-width: 16px;
	width: 16px;	
	height: 10px;
	min-height:10px;
	font-size: 14px;
	font-weight: lighter;		
	position: absolute;
	top: 5px;
	right: 25px; 	
}
.btn-negate.z-button:active, .btn-negate.z-button:focus {
	border: none;
	box-shadow: none;
}
.btn-negate.z-button [class^="z-icon-"] {
	font-size: 14px;
	padding: 0px;
	line-height: 14px;
}

