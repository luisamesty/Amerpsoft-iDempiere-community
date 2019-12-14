<%@ page contentType="text/css;charset=UTF-8" %>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>
<%@ taglib uri="http://www.idempiere.org/dsp/web/util" prefix="u" %>

html,body {
	margin: 0;
	padding: 0;
	height: 100%;
	width: 100%;
	background-color: #D4E3F4;
	color: #333;
	font-family: Helvetica,Arial,sans-serif;
	overflow: hidden;
}

.z-html p{
	margin:0px;
}

<%-- Tablet --%>
.tablet-scrolling {
	-webkit-overflow-scrolling: touch;
}

<%-- vbox fix for firefox and ie --%>
table.z-vbox > tbody > tr > td > table {
	width: 100%;	
}

<%-- workflow activity --%>
.workflow-activity-form {
}
.workflow-panel-table {
	border: 0px;
}

<%-- payment form --%>
.payment-form-content {
}

<%-- Begin EGS Style --%>
.z-listheader-content {
    font-weight: 700;
    font-size: 11px;
    color: #000;
}

<%-- pintal el HR heder de las grillas --%>
.z-listheader {
    border-left: 1px solid #cfcfcf !important;
    border-bottom: 1px solid #cfcfcf !important;
    background: -moz-linear-gradient(top,#fefefe 0,#ECE9D7 40%) ;
    background: -webkit-gradient(linear,left top,left bottom,color-stop(0%,#fefefe),color-stop(40%,#ECE9D7)) !important;
    background: -webkit-linear-gradient(top,#fefefe 0,#ECE9D7 40%) !important;
    background: -o-linear-gradient(top,#fefefe 0,#ECE9D7 40%) !important;
    background: -ms-linear-gradient(top,#fefefe 0,#ECE9D7 40%) !important;
    background: linear-gradient(to bottom,#fefefe 0,#ECE9D7 40%) !important;

}

.z-textbox {
    background-color: #FFFFFF;
    border: 1px solid #0099ff; <%-- cdd7bb --%>
    color: #333;
}

<%-- Define el color de la linea de las grillas --%>
.z-listitem.z-listitem-selected>.z-listcell {
    background: #FBD87E !important;
}
<%-- Define altura del contenido de la grilla --%>
.z-listcell-content {
    line-height: 15px !important;
}

<%-- Define fondo celeste en formulario filtrado--%>
.z-vlayout-inner {
    background-color: #DFEBF5;
}


.z-listbox-odd.z-listitem {
    background: #f5f5f5!important; <%-- f5f5f5 --%>
}

.z-panel-noborder.z-panel-noframe .z-panelchildren {
    background-color: transparent;
}

.desktop-user-panel .z-vbox-separator {
    background-color: transparent;
}

.desktop-left-column .z-caption, .desktop-left-column .z-caption-content {
    background-color: #3A5CAA;
    color: #ffffff !important;
}

.desktop-left-column .z-panel-icon {
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
    background: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zd�IgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCN6a2llOSkiIC8+PC9zdmc+);
    background: -moz-linear-gradient(top,#fefefe 0,#eee 100%);
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

.z-center-body .z-caption {
    border-bottom: solid 2px #FCC654;
}

.favourites-box .z-hbox:hover {
    background-color: #FBD87E;
}

.favourites-box .z-hbox {
    border: solid 1px #ccc;
    background-color: #DFEBF5;
    cursor: pointer;
}

.favourites-box .z-vbox-separator {
    height: .1em;
    background-color: #ffffff;
}
<%-- Fondo de las ventanas Info --%>
.z-north, .z-south, .z-west, .z-center, .z-east {
    border: 1px solid #cfcfcf;
    background: #DFEBF5;
    position: absolute;
    overflow: hidden;
}

.recentitems-box .z-vbox-separator {
    height: .2em;
    background-color: #ffffff;
}

.recentitems-box A:hover {
    background-color: #FBD87E;
}

.recentitems-box A {
    border: solid 1px #ccc;
    background-color: #DFEBF5;
    cursor: pointer;
}

<%-- EGS ScrollBar --%>    

<%-- FIN EGS ScrollBar --%> 

::-webkit-scrollbar {
    width: 12px;
}  

::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5);
    background-color: #9EC8EF;
    background-image: -webkit-linear-gradient(45deg, rgba(255, 255, 255, .2) 25%, transparent 25%, transparent 50%, rgba(255, 255, 255, .2) 50%, rgba(255, 255, 255, .2) 75%, transparent 75%, transparent);
} 

::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
    border-radius: 10px;
    background-color: #f5f5f5;
}

.z-combobox-disabled * {
   background-color: #D9E0EB !important;
   border: solid 1px #CBD0D5 !important;
   box-shadow: 1px 1px 1px #ccc;
}

/* Seccion MENU */
.z-treerow [class^="z-icon-"], .z-treerow [class*=" z-icon-"]{
    display: inline-block;
    background-image: url(/webui/theme/businessTeamBlue/images/arrow-toggle.png) !important;
    width: 18px;
    height: 18px;
    font-size: 15px;
    color: transparent;
}

.z-treerow .z-treecell-text {
    padding-left: 2px;
}


.z-treerow div.z-treecell-content{
    color: #fff;
    background: #606C7D;
}

.z-popup-content { /* border padding a la seccion men�*/
    background: #606C7D;
}

.z-treerow div.z-treecell-content{
    color: #fff;
    background: #606C7D;
}

.z-treerow .menu-href {
    color: #fff !important;
}

.z-treerow .z-treecell-text {
    color: #fff;
}

.z-treerow .z-treecell-text:hover {
    color: #fbd87e;
}

.z-treerow .menu-href:hover, .z-treerow .menu-href:active {
    color: #fbd87e !important;
    text-decoration: none !important;
}

.desktop-menu-popup .z-popup-content {
    width: 320px !important;
    height: 620px !important;
}
.desktop-menu-popup {
    width: 320px !important;
    height: 620px !important;
}

.z-treerow {
  background: transparent;
}

.z-tree-body {
    background: #606C7D;
   /* width: 300px !important;*/
    height: 600px !important;
}

.z-popup-content .z-panel-body {
    background-color: #606C7D !important;
}

.z-window-content .z-center-body { /*Fondo para graficos nativos*/
    background-color: #fff;
}

.z-listbox-body {  /*Fondo para data grid ventanas Info*/
    background-color: #ffffff;
}

.z-row:hover > .z-row-inner, .z-row:hover > .z-cell { /*Vuelve transparente el Hover de las grillas evita efecto sombreado*/
    background-color: transparent !important;
    background: transparent;
        background-color: transparent !important;
}

.adwindow-detailpane-tabbox { /*color fondo pestanhas detalle*/
    background-color: #EEF1F6;
}

/*Secci�n de b�squeda Menu*/

.global-search-tabpanel .z-listbox-body {    /* Existe en ThemeCSS */
    background-color: #606C7D;
}

.global-search-tabpanel .z-listitem:hover > .z-listcell {
    background: #FFF7A5 !important;
}

.global-search-tabpanel .z-listbox-body .z-listitem.z-listitem:hover > .z-listcell > .z-listcell-content {
    color: #636363 !important;
    font-weight: 700 !important;
}
.global-search-tabpanel .z-listbox-body .z-listitem.z-listitem-selected:hover > .z-listcell > .z-listcell-content {
    color: #636363 !important;
	font-weight: 700 !important;
    
}

.global-search-tabpanel div.z-listbox-body .z-listcell {
    padding: 0px;
}

.global-search-tabpanel .z-listitem.z-listitem-selected > .z-listcell {
    background: transparent !important;
}

.global-search-tabpanel .z-row .z-cell, .z-listitem .z-listcell, .z-listitem.z-listitem-selected > .z-listcell {
    border-left: 0px solid #C3DAF9 !important;
}

.global-search-tabpanel .z-listbox-body .z-listitem.z-listitem-selected:active > .z-listcell > .z-listcell-content {
    color: #ffffff !important;
}

.global-search-tabpanel .z-listbox-body .z-listitem.z-listitem-selected > .z-listcell > .z-listcell-content {
    color: #ffffff !important;
}

.global-search-tabpanel .z-listcell > div.z-listcell-content {
    color: #fff !important;
}

.info-panel .z-listbox-body .z-listitem.z-listitem > .z-listcell > .z-listcell-content {
    color: #636363;
}

.info-panel td.z-listcell {
    border-bottom: 0.01em solid #CDD7BB !important;
    border-left: 0.01em solid #CDD7BB !important;
    border-left: none;
}


.z-listitem.z-listitem:hover > .z-listcell {
    background: #FBD87E !important;
}


.z-listbox-body .z-listitem {
    background: transparent;
}

.z-listbox-body .z-listbox-odd.z-listitem {
    background-color: transparent !important;
}

.z-listbox-body .z-listitem.z-listitem-selected>.z-listcell>.z-listcell-content {
    color: #636363 !important;
}

/*header de lso hd en las grillas de las ordenes*/

.z-grid-header table th, .z-grid-header table td {
    background-clip: padding-box;
    padding: 0;
    border-left: 1px solid #cfcfcf !important;
    border-bottom: 1px solid #cfcfcf !important;
    background: -moz-linear-gradient(top,#fefefe 0,#ECE9D7 40%);
    background: -webkit-gradient(linear,left top,left bottom,color-stop(0%,#fefefe),color-stop(40%,#ECE9D7)) !important;
    background: -webkit-linear-gradient(top,#fefefe 0,#ECE9D7 40%) !important;
    background: -o-linear-gradient(top,#fefefe 0,#ECE9D7 40%) !important;
    background: -ms-linear-gradient(top,#fefefe 0,#ECE9D7 40%) !important;
    background: linear-gradient(to bottom,#fefefe 0,#ECE9D7 40%) !important;
}

/*Efecto de fondo en el detalle de la grilla antes de pintar*/

.adwindow-detailpane-tabpanel .z-grid-body {
    margin-top: auto;
    position: relative;
    overflow: hidden;
    border: solid 1px #999 !important;
    background: #fff;
}

adwindow-detailpane-tabpanel .z-grid tbody tr td.z-cell { /*Color border grilla detalle Sales Order*/
    background-image: none !important;
    border: 0.1em solid #CDD7BB !important;
}

/*Tab de-selected main*/

.desktop-tabbox .z-tab::before {
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
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    -webkit-transform: skew(10deg);
    -moz-transform: skew(10deg);
    -ms-transform: skew(10deg);
    -o-transform: skew(10deg);
    transform: skew(-20deg);
    /* -webkit-border-radius: 0 5px 0 0; */
    -moz-border-radius: 0 5px 0 0;
    /* border-radius: 0 5px 0 0; */
    background: #f0f9ff;
    background: -moz-linear-gradient(top, #f0f9ff 15%, #c3d4ea 30%, #9db7db 100%);
    background: -webkit-linear-gradient(top, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%);
    background: linear-gradient(to bottom, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%) filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f0f9ff', endColorstr='#9db7db',GradientType=0 );
    border-left: solid 3px #9AADC5;
}

.adwindow-form .z-button {
	color: #636363;
    text-shadow: 0px 0px 1px #fff;
    font-weight: 600;
    background: rgb(252,234,187);
    background: -moz-linear-gradient(top, rgba(252,234,187,1) 0%, rgba(252,205,77,1) 50%, rgba(248,181,0,1) 51%, rgba(251,223,147,1) 100%);
    background: -webkit-linear-gradient(top, rgba(252,234,187,1) 0%,rgba(252,205,77,1) 50%,rgba(248,181,0,1) 51%,rgba(251,223,147,1) 100%);
    background: linear-gradient(to bottom, rgba(252,234,187,1) 0%,rgba(252,205,77,1) 50%,rgba(248, 181, 0, 0.45) 51%,rgba(251,223,147,1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#fceabb', endColorstr='#fbdf93',GradientType=0 );
}

.tab-editor-form .z-listcell-content { /* Color de las grillas en editores*/
    color: #636363 !important;
}

.z-center-body .grid-layout {

   background-color: #ffffff 

}

/* Efecto tabs en las sub-consultas */
/*.z-tabbox-top>.z-tabs .z-tab:first-child.z-tab-selected {
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
}*/

.popup-dialog .z-tab-selected .z-tab-text {
    color: #fff !important;
}

.popup-dialog .z-tabbox-left-scroll, .z-tabbox-right-scroll {
    background-color: #5AA7F6 !important;
    background: #5AA7F6 !important;
    color: #fff !important;
    width: 20px;
}

.popup-dialog .z-tabbox-scroll > .z-tabs {
    background: #DFEBF5 !important;
}

.popup-dialog td.z-listcell {
    border-bottom: 0.01em solid #CDD7BB !important;
    border-left: 0.01em solid #CDD7BB !important;
    border-left: none;
}

.popup-dialog .z-north {
    line-height: 24px;
    background: #ffffff !important;
}

.popup-dialog .z-north-body .z-label {
    color: #17181a;
    font-family: Helvetica,Arial,sans-serif;
    margin-left: 20px;
    font-weight: 700;
    margin-top: 20px;
    line-height: 40px;
    font-size: 16px;
}
    
.popup-dialog .z-tab{
    color: #636363;
    display: block;
    -webkit-box-shadow: 1px 1px 0 #fff;
    -moz-box-shadow: 1px 1px 0 #fff;
    -o-box-shadow: 1px 1px 0 #fff;
    -ms-box-shadow: 1px 1px 0 #fff;
    box-shadow: 1px 1px 0 #fff;
    padding-top: 0px;
    background: #f0f9ff;
    background: -moz-linear-gradient(top, #f0f9ff 15%, #c3d4ea 30%, #9db7db 100%);
    background: -webkit-linear-gradient(top, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%);
    background: linear-gradient(to bottom, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%) filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f0f9ff', endColorstr='#9db7db',GradientType=0 );
    border: solid 1px #9AADC5;
}

.popup-dialog .z-tab-selected{
    cursor: default;
    font-weight: bold;
    color: #fff;
    font-weight: 700;
    font-family: Helvetica,Arial,sans-serif;
    background-image: -moz-linear-gradient(top, #fff, #ddd);
    background-image: -ms-linear-gradient(top, #fff, #ddd);
    background-image: -o-linear-gradient(top, #fff, #ddd);
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.4);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    background: #7abcff !important;
    background: -moz-linear-gradient(top, #7abcff 0%, #60abf8 44%, #4096ee 100%) !important;
    background: -webkit-linear-gradient(top, #7abcff 0%,#60abf8 44%,#4096ee 100%) !important;
    background: linear-gradient(to bottom, #7abcff 30%,#60abf8 20%,#4096ee 100%) !important;
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7abcff', endColorstr='#4096ee',GradientType=0) !important;
}

.popup-dialog .z-tab-selected::before{
    content: '' !important;
    position: absolute !important;
    z-index: 1 !important;
	top: -1px !important;
    left: -.5em !important;
    bottom: -0.05em !important;
    width: 1em !important;
    background-image: -moz-linear-gradient(top, #fff, #ddd) !important;
    background-image: -ms-linear-gradient(top, #fff, #ddd) 
    background-image: -o-linear-gradient(top, #fff, #ddd) 
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.4) !important;
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.4) !important;
    box-shadow: 2px 2px 2px rgba(0,0,0,.0) !important;
    -webkit-transform: skew(10deg)!important;
    -moz-transform: skew(10deg) !important;
    -ms-transform: skew(10deg) !important;
    -o-transform: skew(10deg) !important;
    transform: skew(-20deg) !important;
    -webkit-border-radius: 0 5px 0 0 !important;
    -moz-border-radius: 0 5px 0 0 !important;
    border-radius: 0 5px 0 0 !important;
    background: #7abcff !important;;
    background: -moz-linear-gradient(top, #7abcff 0%, #60abf8 44%, #4096ee 100%) !important;
    background: -webkit-linear-gradient(top, #7abcff 0%,#60abf8 44%,#4096ee 100%) !important;
    background: linear-gradient(to bottom, #7abcff 30%,#60abf8 20%,#4096ee 100%) !important;
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#7abcff', endColorstr='#4096ee',GradientType=0 ) !important;
}

.popup-dialog .z-tab::before{
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
    -moz-box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    -webkit-box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    box-shadow: 2px 2px 2px rgba(0,0,0,.0);
    -webkit-transform: skew(10deg);
    -moz-transform: skew(10deg);
    -ms-transform: skew(10deg);
    -o-transform: skew(10deg);
    transform: skew(-20deg);
    /* -webkit-border-radius: 0 5px 0 0; */
    -moz-border-radius: 0 5px 0 0;
    /* border-radius: 0 5px 0 0; */
    background: #f0f9ff;
    background: -moz-linear-gradient(top, #f0f9ff 15%, #c3d4ea 30%, #9db7db 100%);
    background: -webkit-linear-gradient(top, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%);
    background: linear-gradient(to bottom, #f0f9ff 15%,#c3d4ea 30%,#9db7db 100%) filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f0f9ff', endColorstr='#9db7db',GradientType=0 );
    border-left: solid 3px #9AADC5;
}

.adwindow-form .editor-button {
    background: none !important;
  //  background-color: rgba(244, 58, 58, 0.95) !important;
}

.info-panel .z-window-header {
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

<%-- End EGS Style --%>    

<c:include page="fragment/login.css.dsp" />

<c:include page="fragment/desktop.css.dsp" />

<c:include page="fragment/application-menu.css.dsp" />

<c:include page="fragment/gadget.css.dsp" />

<c:include page="fragment/toolbar.css.dsp" />

<c:include page="fragment/button.css.dsp" />

<c:include page="fragment/adwindow.css.dsp" />
			
<c:include page="fragment/grid.css.dsp" />

<c:include page="fragment/input-element.css.dsp" />

<c:include page="fragment/tree.css.dsp" /> 

<c:include page="fragment/field-editor.css.dsp" />

<c:include page="fragment/group.css.dsp" />

<c:include page="fragment/tab.css.dsp" />

<c:include page="fragment/menu-tree.css.dsp" />

<c:include page="fragment/info-window.css.dsp" />

<c:include page="fragment/window.css.dsp" />

<c:include page="fragment/form.css.dsp" />

<c:include page="fragment/toolbar-popup.css.dsp" />	

<c:include page="fragment/setup-wizard.css.dsp" />

<c:include page="fragment/about.css.dsp" />

<c:include page="fragment/tab-editor.css.dsp" />

<c:include page="fragment/find-window.css.dsp" />

<c:include page="fragment/help-window.css.dsp" />

<c:include page="fragment/borderlayout.css.dsp" />

<c:include page="fragment/parameter-process.css.dsp" />

<c:include page="fragment/font-icons.css.dsp" />
