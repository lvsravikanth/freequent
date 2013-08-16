<%--
/*######################################################################################
Copyright 2008 Vignette Corporation. All rights reserved. This software
is an unpublished work and trade secret of Vignette, and distributed only
under restriction. This software (or any part of it) may not be used,
modified, reproduced, stored on a retrieval system, distributed, or
transmitted without the express written consent of Vignette. Violation of
the provisions contained herein may result in severe civil and criminal
penalties, and any violators will be prosecuted to the maximum extent
possible under the law. Further, by using this software you acknowledge and
agree that if this software is modified by anyone such as you, a third party
or Vignette, then Vignette will have no obligation to provide support or
maintenance for this software.
#####################################################################################*/
--%>
<%@ page contentType="text/javascript;charset=UTF-8"%>

<%@ page import="com.vignette.ui.core.l10n.UIResource"%>
<%@ page import="com.vignette.ui.vcm.l10n.WorkspaceResource"%>
<%@ page import="com.vignette.ui.vcm.l10n.WizardResource"%>
<%@ page import="com.vignette.ui.vcm.l10n.module.SearchResource"%>
<%@ page import="com.vignette.ui.vcm.l10n.EditorResource"%>
<%@ page import="com.vignette.ui.vcm.l10n.WidgetResource"%>
<%@ page import="com.vignette.ui.vcm.l10n.PropertiesResource"%>

<%@ taglib prefix="core" uri="http://ui.vignette.com/core" %>

<%-- UI --%>
<core:js-messages basename="<%= UIResource.BASE_NAME %>"/>

<%-- Workspace --%>
<core:js-messages basename="<%= WorkspaceResource.BASE_NAME %>"/>

<%-- Wizard --%>
<core:js-messages basename="<%= WizardResource.BASE_NAME %>"/>

<%-- Search --%>
<core:js-messages basename="<%= SearchResource.BASE_NAME %>"/>

<%-- Editor --%>
<core:js-messages basename="<%= EditorResource.BASE_NAME %>"/>

<%-- Widget --%>
<core:js-messages basename="<%= WidgetResource.BASE_NAME %>"/>

<%-- Properties --%>
<core:js-messages basename="<%= PropertiesResource.BASE_NAME %>"/>

<%-- Global --%>
<core:global-js-messages/>
