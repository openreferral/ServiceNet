import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import scheduler, { SchedulerState } from 'app/modules/administration/scheduler/scheduler.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
import sessions, { SessionsState } from 'app/modules/account/sessions/sessions.reducer';
// prettier-ignore
import systemAccount, {
SystemAccountState
 } from 'app/entities/system-account/system-account.reducer';
// prettier-ignore
import documentUpload, {
DocumentUploadState
 } from 'app/entities/document-upload/document-upload.reducer';
// prettier-ignore
import organization, {
OrganizationState
 } from 'app/entities/organization/organization.reducer';
// prettier-ignore
import service, {
ServiceState
 } from 'app/entities/service/service.reducer';
// prettier-ignore
import program, {
ProgramState
 } from 'app/entities/program/program.reducer';
// prettier-ignore
import serviceAtLocation, {
ServiceAtLocationState
 } from 'app/entities/service-at-location/service-at-location.reducer';
// prettier-ignore
import location, {
LocationState
 } from 'app/entities/location/location.reducer';
// prettier-ignore
import physicalAddress, {
PhysicalAddressState
 } from 'app/entities/physical-address/physical-address.reducer';
// prettier-ignore
import postalAddress, {
PostalAddressState
 } from 'app/entities/postal-address/postal-address.reducer';
// prettier-ignore
import phone, {
PhoneState
 } from 'app/entities/phone/phone.reducer';
// prettier-ignore
import contact, {
ContactState
 } from 'app/entities/contact/contact.reducer';
// prettier-ignore
import regularSchedule, {
RegularScheduleState
 } from 'app/entities/regular-schedule/regular-schedule.reducer';
// prettier-ignore
import holidaySchedule, {
HolidayScheduleState
 } from 'app/entities/holiday-schedule/holiday-schedule.reducer';
// prettier-ignore
import funding, {
FundingState
 } from 'app/entities/funding/funding.reducer';
// prettier-ignore
import eligibility, {
EligibilityState
 } from 'app/entities/eligibility/eligibility.reducer';
// prettier-ignore
import serviceArea, {
ServiceAreaState
 } from 'app/entities/service-area/service-area.reducer';
// prettier-ignore
import requiredDocument, {
RequiredDocumentState
 } from 'app/entities/required-document/required-document.reducer';
// prettier-ignore
import paymentAccepted, {
PaymentAcceptedState
} from 'app/entities/payment-accepted/payment-accepted.reducer';
// prettier-ignore
import language, {
LanguageState
 } from 'app/entities/language/language.reducer';
// prettier-ignore
import accessibilityForDisabilities, {
AccessibilityForDisabilitiesState
 } from 'app/entities/accessibility-for-disabilities/accessibility-for-disabilities.reducer';
// prettier-ignore
import serviceTaxonomy, {
ServiceTaxonomyState
 } from 'app/entities/service-taxonomy/service-taxonomy.reducer';
// prettier-ignore
import taxonomy, {
TaxonomyState
 } from 'app/entities/taxonomy/taxonomy.reducer';
// prettier-ignore
import organizationMatch, {
OrganizationMatchState
 } from 'app/entities/organization-match/organization-match.reducer';
// prettier-ignore
import metadata, {
MetadataState
} from 'app/entities/metadata/metadata.reducer';
// prettier-ignore
import openingHours, {
  OpeningHoursState
} from 'app/entities/opening-hours/opening-hours.reducer';
// prettier-ignore
import uploadPage, {
  UploadPageState
} from 'app/modules/upload/upload-page.reducer';
// prettier-ignore
import sharedRecordView, {
  SharedRecordViewState
} from 'app/modules/conflicts/shared/shared-record-view.reducer';
// prettier-ignore
import conflict, {
  ConflictState
} from 'app/entities/conflict/conflict.reducer';
// prettier-ignore
import dataImportReport, {
  DataImportReportState
} from 'app/entities/data-import-report/data-import-report.reducer';
// prettier-ignore
import activity, {
  ActivityState
} from 'app/shared/reducers/activity.reducer';
// prettier-ignore
import fieldExclusion, {
  FieldExclusionState
} from 'app/entities/field-exclusion/field-exclusion.reducer';
// prettier-ignore
import exclusionsConfig, {
  ExclusionsConfigState
} from 'app/entities/exclusions-config/exclusions-config.reducer';
// prettier-ignore
import geocodingResult, {
  GeocodingResultState
} from 'app/entities/geocoding-result/geocoding-result.reducer';
import filterActivity, { FilterActivityState } from 'app/modules/home/filter-activity.reducer';
// prettier-ignore
import filterShelter, { FilterShelterState } from 'app/modules/shelter/filter-shelter.reducer';
// prettier-ignore
import beds, {
  BedsState
} from 'app/entities/beds/beds.reducer';
// prettier-ignore
import shelter, {
  ShelterState
} from 'app/entities/shelter/shelter.reducer';
// prettier-ignore
import option, {
  OptionState
} from 'app/entities/option/option.reducer';
// prettier-ignore
import locationExclusion, {
  LocationExclusionState
} from 'app/entities/location-exclusion/location-exclusion.reducer';
// prettier-ignore
import organizationError, {
  OrganizationErrorState
} from 'app/entities/organization-error/organization-error.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly scheduler: SchedulerState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly sessions: SessionsState;
  readonly systemAccount: SystemAccountState;
  readonly documentUpload: DocumentUploadState;
  readonly organization: OrganizationState;
  readonly service: ServiceState;
  readonly program: ProgramState;
  readonly serviceAtLocation: ServiceAtLocationState;
  readonly location: LocationState;
  readonly physicalAddress: PhysicalAddressState;
  readonly postalAddress: PostalAddressState;
  readonly phone: PhoneState;
  readonly contact: ContactState;
  readonly regularSchedule: RegularScheduleState;
  readonly holidaySchedule: HolidayScheduleState;
  readonly funding: FundingState;
  readonly eligibility: EligibilityState;
  readonly serviceArea: ServiceAreaState;
  readonly requiredDocument: RequiredDocumentState;
  readonly paymentAccepted: PaymentAcceptedState;
  readonly language: LanguageState;
  readonly accessibilityForDisabilities: AccessibilityForDisabilitiesState;
  readonly serviceTaxonomy: ServiceTaxonomyState;
  readonly taxonomy: TaxonomyState;
  readonly organizationMatch: OrganizationMatchState;
  readonly metadata: MetadataState;
  readonly openingHours: OpeningHoursState;
  readonly uploadPage: UploadPageState;
  readonly sharedRecordView: SharedRecordViewState;
  readonly conflict: ConflictState;
  readonly dataImportReport: DataImportReportState;
  readonly activity: ActivityState;
  readonly filterActivity: FilterActivityState;
  readonly fieldExclusion: FieldExclusionState;
  readonly exclusionsConfig: ExclusionsConfigState;
  readonly geocodingResult: GeocodingResultState;
  readonly beds: BedsState;
  readonly shelter: ShelterState;
  readonly option: OptionState;
  readonly locationExclusion: LocationExclusionState;
  readonly organizationError: OrganizationErrorState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
  readonly filterShelter: FilterShelterState;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  scheduler,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  systemAccount,
  documentUpload,
  organization,
  service,
  program,
  serviceAtLocation,
  location,
  physicalAddress,
  postalAddress,
  phone,
  contact,
  regularSchedule,
  holidaySchedule,
  funding,
  eligibility,
  serviceArea,
  requiredDocument,
  paymentAccepted,
  language,
  accessibilityForDisabilities,
  serviceTaxonomy,
  taxonomy,
  organizationMatch,
  metadata,
  openingHours,
  uploadPage,
  sharedRecordView,
  conflict,
  dataImportReport,
  activity,
  fieldExclusion,
  exclusionsConfig,
  geocodingResult,
  beds,
  shelter,
  option,
  locationExclusion,
  organizationError,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
  filterActivity,
  filterShelter
});

export default rootReducer;
