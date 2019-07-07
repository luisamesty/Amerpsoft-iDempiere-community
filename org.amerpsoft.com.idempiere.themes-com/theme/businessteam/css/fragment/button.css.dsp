.z-button {
  display: inline-block;
  margin: 0px;
  padding: 4px 20px;
  font-size: 12px;
  line-height: 20px;
  text-align: center;
  vertical-align: middle;
  cursor: pointer;
  /*background-color: #f5f5f5;
  background-image: -moz-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#e6e6e6));
  background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: -o-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: linear-gradient(to bottom, #ffffff, #e6e6e6);
  background-repeat: repeat-x;*/
  
  /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#f5e095+1,ebce83+100 */
background: rgb(245,224,149); /* Old browsers */
background: -moz-linear-gradient(top,  rgba(245,224,149,1) 1%, rgba(235,206,131,1) 100%); /* FF3.6-15 */
background: -webkit-linear-gradient(top,  rgba(245,224,149,1) 1%,rgba(235,206,131,1) 100%); /* Chrome10-25,Safari5.1-6 */
background: linear-gradient(to bottom,  rgba(245,224,149,1) 1%,rgba(235,206,131,1) 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5e095', endColorstr='#ebce83',GradientType=0 ); /* IE6-9 */
  
  
  border: 1px solid #cccccc;
  border-color: #e6e6e6 #e6e6e6 #bfbfbf;
  border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
  border-bottom-color: #b3b3b3;
  -webkit-border-radius: 4px;
     -moz-border-radius: 4px;
          border-radius: 4px;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffffff', endColorstr='#ffe6e6e6', GradientType=0);
  filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
  zoom: 1;
  -webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
     -moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
          box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
}

.z-button:hover {
    border-color: #fb9d23;
    background: rgb(245,224,149);
    background: -moz-linear-gradient(top, rgba(245,224,149,1) 1%, rgba(235,206,131,1) 100%);
    background: -webkit-linear-gradient(top, rgba(245,224,149,1) 1%,rgba(235,206,131,1) 100%);
    background: linear-gradient(to bottom, rgba(245,224,149,1) 1%,rgba(235,206,131,1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5e095', endColorstr='#ebce83',GradientType=0 );
}

.z-button:active {
 border-color: #fb9d23;
    background: rgb(245,224,149);
    background: -moz-linear-gradient(top, rgba(245,224,149,1) 1%, rgba(235,206,131,1) 100%);
    background: -webkit-linear-gradient(top, rgba(245,224,149,1) 1%,rgba(235,206,131,1) 100%);
    background: linear-gradient(to bottom, rgba(245,224,149,1) 1%,rgba(235,206,131,1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5e095', endColorstr='#ebce83',GradientType=0 );
}

.z-button {
	margin: 0px !important;
}

.z-button-hover,
.z-button-focus,
.z-button-click,
.z-button-disabled {
  color: #333333;
  background-color: #e6e6e6;
}

.z-button-click {
  background-color: #cccccc;
}

.z-button-hover,
.z-button-focus {
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
