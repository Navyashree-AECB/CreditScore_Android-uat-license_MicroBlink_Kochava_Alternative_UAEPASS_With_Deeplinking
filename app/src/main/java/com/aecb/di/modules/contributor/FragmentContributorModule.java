package com.aecb.di.modules.contributor;

import com.aecb.di.modules.AddCvvFragmentModule;
import com.aecb.di.modules.AddDobPassportModule;
import com.aecb.di.modules.ContactUsModule;
import com.aecb.di.modules.ContactUsRegistrationModule;
import com.aecb.di.modules.CreditReportFragmentModule;
import com.aecb.di.modules.DataCorrectionModule;
import com.aecb.di.modules.DownloadPdfModule;
import com.aecb.di.modules.EditProfileFragmentModule;
import com.aecb.di.modules.ExitFragmentModule;
import com.aecb.di.modules.FAQFragmentModule;
import com.aecb.di.modules.GlossaryFragmentModule;
import com.aecb.di.modules.HelpSupportModule;
import com.aecb.di.modules.HistoryModule;
import com.aecb.di.modules.HomeModule;
import com.aecb.di.modules.IntroFourModule;
import com.aecb.di.modules.IntroFragmentModule;
import com.aecb.di.modules.IntroOneModule;
import com.aecb.di.modules.IntroThreeModule;
import com.aecb.di.modules.IntroTwoModule;
import com.aecb.di.modules.NoScoreModule;
import com.aecb.di.modules.NotificationModule;
import com.aecb.di.modules.OtpVerifyModule;
import com.aecb.di.modules.PaymentUpdateDeleteModule;
import com.aecb.di.modules.ProductsMenuModule;
import com.aecb.di.modules.PurchaseOptionsModule;
import com.aecb.di.modules.ReportHistoryModule;
import com.aecb.di.modules.ScoreHistoryModule;
import com.aecb.di.modules.SearchModule;
import com.aecb.di.modules.TermsAndConditionsModule;
import com.aecb.di.modules.TouchFragmentModule;
import com.aecb.di.modules.TouchIdModule;
import com.aecb.di.modules.UpdateRecipientsModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.addcvv.view.AddCvvFragment;
import com.aecb.ui.addpassportdob.view.AddDobPassportFragment;
import com.aecb.ui.contactusregistration.view.ContactUsRegistrationFragment;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.creditreport.faq.view.FAQFragment;
import com.aecb.ui.creditreport.glossary.view.GlossaryFragment;
import com.aecb.ui.creditreport.intro.view.IntroFragment;
import com.aecb.ui.downloadpdf.view.DownloadPdfFragment;
import com.aecb.ui.editprofile.view.EditProfileFragment;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.helpandsupport.contactus.view.ContactUsFragment;
import com.aecb.ui.helpandsupport.datacorrection.view.DataCorrectionFragment;
import com.aecb.ui.helpandsupport.view.HelpSupportFragment;
import com.aecb.ui.history.view.HistoryFragment;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.ui.introscreen.introfour.view.IntroFourFragment;
import com.aecb.ui.introscreen.introone.view.IntroOneFragment;
import com.aecb.ui.introscreen.introthree.view.IntroThreeFragment;
import com.aecb.ui.introscreen.introtwo.view.IntroTwoFragment;
import com.aecb.ui.loginflow.touchid.view.TouchIdFragment;
import com.aecb.ui.noscorefragment.view.NoScoreFragment;
import com.aecb.ui.notification.view.NotificationFragment;
import com.aecb.ui.productsmenu.view.ProductsMenuFragment;
import com.aecb.ui.purchasejourney.cardupdatedeletefragment.view.PaymentUpdateDeleteFragment;
import com.aecb.ui.purchaseoptions.view.PurchaseOptionsFragment;
import com.aecb.ui.registerflow.otpverify.view.OtpVerifyFragment;
import com.aecb.ui.reporthistory.view.ReportHistoryFragment;
import com.aecb.ui.scorehistory.view.ScoreHistoryFragment;
import com.aecb.ui.searchdialog.view.SearchFragment;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.ui.touchdialog.view.TouchFragment;
import com.aecb.ui.updaterecipients.view.UpdateRecipientsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class FragmentContributorModule {

    @ContributesAndroidInjector(modules = {TermsAndConditionsModule.class})
    @FragmentScope
    public abstract TermsAndConditionsFragment termsAndConditionsFragment();

    @ContributesAndroidInjector(modules = {OtpVerifyModule.class})
    @FragmentScope
    public abstract OtpVerifyFragment otpVerifyFragment();

    @ContributesAndroidInjector(modules = {TouchIdModule.class})
    @FragmentScope
    public abstract TouchIdFragment touchIdFragment();

    @ContributesAndroidInjector(modules = {SearchModule.class})
    @FragmentScope
    public abstract SearchFragment searchFragment();

    @ContributesAndroidInjector(modules = {HomeModule.class})
    @FragmentScope
    public abstract HomeFragment homeFragment();

    @ContributesAndroidInjector(modules = {HistoryModule.class})
    @FragmentScope
    public abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector(modules = {HelpSupportModule.class})
    @FragmentScope
    public abstract HelpSupportFragment helpSupportFragment();

    @ContributesAndroidInjector(modules = {ReportHistoryModule.class})
    @FragmentScope
    public abstract ReportHistoryFragment reportHistoryFragment();

    @ContributesAndroidInjector(modules = {ScoreHistoryModule.class})
    @FragmentScope
    public abstract ScoreHistoryFragment scoreHistoryFragment();

    @ContributesAndroidInjector(modules = {PurchaseOptionsModule.class})
    @FragmentScope
    public abstract PurchaseOptionsFragment purchaseOptionsFragment();

    @ContributesAndroidInjector(modules = {UpdateRecipientsModule.class})
    @FragmentScope
    public abstract UpdateRecipientsFragment updateRecipientsFragment();

    @ContributesAndroidInjector(modules = {ProductsMenuModule.class})
    @FragmentScope
    public abstract ProductsMenuFragment productsMenuFragment();

    @ContributesAndroidInjector(modules = {PaymentUpdateDeleteModule.class})
    @FragmentScope
    public abstract PaymentUpdateDeleteFragment paymentUpdateDeleteFragment();

    @ContributesAndroidInjector(modules = {ExitFragmentModule.class})
    @FragmentScope
    public abstract ExitFragment exitFragment();

    @ContributesAndroidInjector(modules = {CreditReportFragmentModule.class})
    @FragmentScope
    public abstract CreditReportFragment creditReportFragment();

    @ContributesAndroidInjector(modules = {IntroFragmentModule.class})
    @FragmentScope
    public abstract IntroFragment introFragment();

    @ContributesAndroidInjector(modules = {FAQFragmentModule.class})
    @FragmentScope
    public abstract FAQFragment faqFragment();

    @ContributesAndroidInjector(modules = {GlossaryFragmentModule.class})
    @FragmentScope
    public abstract GlossaryFragment glossaryFragment();

    @ContributesAndroidInjector(modules = {DownloadPdfModule.class})
    @FragmentScope
    public abstract DownloadPdfFragment downloadPdfFragment();

    @ContributesAndroidInjector(modules = {NotificationModule.class})
    @FragmentScope
    public abstract NotificationFragment NotificationFragment();

    @ContributesAndroidInjector(modules = {ContactUsModule.class})
    @FragmentScope
    public abstract ContactUsFragment contactUsFragment();

    @ContributesAndroidInjector(modules = {DataCorrectionModule.class})
    @FragmentScope
    public abstract DataCorrectionFragment dataCorrectionFragment();

    @ContributesAndroidInjector(modules = {NoScoreModule.class})
    @FragmentScope
    public abstract NoScoreFragment noScoreFragment();

    @ContributesAndroidInjector(modules = {EditProfileFragmentModule.class})
    @FragmentScope
    public abstract EditProfileFragment editProfileFragment();

    @ContributesAndroidInjector(modules = {ContactUsRegistrationModule.class})
    @FragmentScope
    public abstract ContactUsRegistrationFragment contactUsRegistrationFragment();

    @ContributesAndroidInjector(modules = {TouchFragmentModule.class})
    @FragmentScope
    public abstract TouchFragment touchFragment();

    @ContributesAndroidInjector(modules = {IntroOneModule.class})
    @FragmentScope
    public abstract IntroOneFragment introOneFragment();

    @ContributesAndroidInjector(modules = {IntroTwoModule.class})
    @FragmentScope
    public abstract IntroTwoFragment introTwoFragment();

    @ContributesAndroidInjector(modules = {IntroThreeModule.class})
    @FragmentScope
    public abstract IntroThreeFragment introThreeFragment();

    @ContributesAndroidInjector(modules = {IntroFourModule.class})
    @FragmentScope
    public abstract IntroFourFragment introFourFragment();

    @ContributesAndroidInjector(modules = {AddCvvFragmentModule.class})
    @FragmentScope
    public abstract AddCvvFragment addCvvFragment();

    @ContributesAndroidInjector(modules = {AddDobPassportModule.class})
    @ActivityScope
    public abstract AddDobPassportFragment addDobPassportFragment();
}
