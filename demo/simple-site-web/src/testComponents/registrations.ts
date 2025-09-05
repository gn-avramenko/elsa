import { registerFactory } from './common-component';
import { WebAppFactory } from './test-webapp';
import { TestNavigationPanelFactory } from './test-navigation';
import { TestStandardLinkFactory } from './test-standard-link';
import { TestAccountLinkFactory } from './test-account-link';
import { TestLanguageSelectorFactory } from './test-language-selector';
import { TestWebAppRouterFactory } from './test-web-app-router';
import { TestHistoryPageFactory } from './test-history-page';
import { TestMainPageFactory } from './test-main-page';
import { TestMeasurementsChartFactory } from './test-measurements-chart';
import { TestAccountContainerFactory } from './test-account-container';
import { TestAccountNavigationPanelFactory } from './test-account-navigation';
import { TestAccountNavigationLinkFactory } from './test-account-navigation-link';
import { TestAccountRouterFactory } from './test-account-router';
import { TestOrganizationsFactory } from './test-organizations-page';
import { TestManagersPageFactory } from './test-managers-page';
import { TestClientsPageFactory } from './test-clients-page';
import { TestSecurityPageFactory } from './test-security-page';
import { TestDoctorsPageFactory } from './test-doctors-page';
import { TestAccountPageFactory } from './test-account-page';
import { TestSearchFieldFactory } from './test-search-field';
import { TestEntityListFactory } from './test-entity-list';
import { TestAutocompleteFieldFactory } from './test-autocomplete-field';
import { TestStandardTextFieldFactory } from './test-standard-text-field';
import { TestOrganizationEditorFactory } from './test-organization-editor';
import { TestWrapperFactory } from './test-wrapper';
import { TestStandardLabelFactory } from './test-standard-label';
import { TestDialogButtonFactory } from './test-dialog-button';
import { TestConfirmDeleteDialogFactory } from './test-confirm-delete-dialog';

registerFactory('app.WebApp', new WebAppFactory());
registerFactory('app.NavigationPanel', new TestNavigationPanelFactory());
registerFactory('app.StandardLink', new TestStandardLinkFactory());
registerFactory('app.AccountLink', new TestAccountLinkFactory());
registerFactory('app.LanguageSelector', new TestLanguageSelectorFactory());
registerFactory('app.WebAppRouter', new TestWebAppRouterFactory());
registerFactory('history.HistoryPage', new TestHistoryPageFactory());
registerFactory('main.MainPage', new TestMainPageFactory());
registerFactory('main.MeasurementsChart', new TestMeasurementsChartFactory());

registerFactory('account.AccountContainer', new TestAccountContainerFactory());
registerFactory('account.AccountNavigation', new TestAccountNavigationPanelFactory());
registerFactory(
    'account.AccountNavigationButton',
    new TestAccountNavigationLinkFactory()
);
registerFactory('account.AccountRouter', new TestAccountRouterFactory());
registerFactory(
    'account.organization.OrganizationsSection',
    new TestOrganizationsFactory()
);
registerFactory('account.manager.ManagersSection', new TestManagersPageFactory());
registerFactory('account.client.ClientsSection', new TestClientsPageFactory());
registerFactory('account.security.SecuritySection', new TestSecurityPageFactory());
registerFactory('account.doctor.DoctorsSection', new TestDoctorsPageFactory());
registerFactory('account.account.AccountSection', new TestAccountPageFactory());
registerFactory(
    'account.organization.OrganizationEditor',
    new TestOrganizationEditorFactory()
);

registerFactory('common.SearchField', new TestSearchFieldFactory());
registerFactory('common.StandardAutocomplete', new TestAutocompleteFieldFactory());
registerFactory('common.StandardTextField', new TestStandardTextFieldFactory());
registerFactory('common.EntityList', new TestEntityListFactory());
registerFactory('wrapper', new TestWrapperFactory());
registerFactory('common.StandardLabel', new TestStandardLabelFactory());
registerFactory('common.DialogButton', new TestDialogButtonFactory());
registerFactory(
    'account.organization.ConfirmDeleteDialog',
    new TestConfirmDeleteDialogFactory()
);
