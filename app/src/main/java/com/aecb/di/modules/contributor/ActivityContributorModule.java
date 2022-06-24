package com.aecb.di.modules.contributor;

import com.aecb.di.modules.AboutUsModule;
import com.aecb.di.modules.AdcbWebviewModule;
import com.aecb.di.modules.AddCardModule;
import com.aecb.di.modules.AddDobPassportModule;
import com.aecb.di.modules.AddRecipientsModule;
import com.aecb.di.modules.CardListModule;
import com.aecb.di.modules.ChangePasswordModule;
import com.aecb.di.modules.CheckoutModule;
import com.aecb.di.modules.ContactDetailsModule;
import com.aecb.di.modules.CreatePasswordModule;
import com.aecb.di.modules.DashboardModule;
import com.aecb.di.modules.DeviceRootedModule;
import com.aecb.di.modules.EDirhamWebviewModule;
import com.aecb.di.modules.ForgotPasswordModule;
import com.aecb.di.modules.IntroMainModule;
import com.aecb.di.modules.LoginActivityModule;
import com.aecb.di.modules.MaintenanceActivityModule;
import com.aecb.di.modules.MenuActivityModule;
import com.aecb.di.modules.PdfViewModule;
import com.aecb.di.modules.PersonalDetailsModule;
import com.aecb.di.modules.ProfileActivityModule;
import com.aecb.di.modules.ScanEmirateIdModule;
import com.aecb.di.modules.SecurityQuestionsRoundOneModule;
import com.aecb.di.modules.SecurityQuestionsRoundTwoModule;
import com.aecb.di.modules.SplashActivityModule;
import com.aecb.di.modules.StartupActivityModule;
import com.aecb.di.modules.TermsAndConditionActivityModule;
import com.aecb.di.modules.UAEPassPinModule;
import com.aecb.di.modules.VerifyAmountModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.aboutus.view.AboutUsActivity;
import com.aecb.ui.adcbwebview.view.AdcbCardWebviewActivity;
import com.aecb.ui.addpassportdob.view.AddDobPassportFragment;
import com.aecb.ui.addrecipients.view.AddRecipientsActivity;
import com.aecb.ui.changepassword.view.ChangePasswordActivity;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.devicerooted.view.DeviceRootedActivity;
import com.aecb.ui.edirhamwebview.view.EDirhamWebviewActivity;
import com.aecb.ui.introscreen.intromain.view.IntroActivity;
import com.aecb.ui.loginflow.forgotpassword.view.ForgotPasswordActivity;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.loginflow.uaepasspin.view.UAEPassPinActivity;
import com.aecb.ui.maintanance.view.MaintenanceActivity;
import com.aecb.ui.menu.view.MenuActivity;
import com.aecb.ui.pdf.view.PdfViewActivity;
import com.aecb.ui.profile.view.ProfileActivity;
import com.aecb.ui.purchasejourney.addcard.view.AddCardActivity;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;
import com.aecb.ui.registerflow.contactdetails.view.ContactDetailsActivity;
import com.aecb.ui.registerflow.createpassword.view.CreatePasswordActivity;
import com.aecb.ui.registerflow.personaldetails.view.PersonalDetailsActivity;
import com.aecb.ui.registerflow.securityquestionsroundone.view.SecurityQuestionsRoundOneActivity;
import com.aecb.ui.registerflow.securityquestionsroundtwo.view.SecurityQuestionsRoundTwoActivity;
import com.aecb.ui.scanemirate.view.ScanEmirateIdActivity;
import com.aecb.ui.splash.view.SplashActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.ui.termsandcodition.view.TermsAndConditionActivity;
import com.aecb.ui.verifyamountforpayment.view.VerifyAmountForPaymentActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class ActivityContributorModule {

    @ContributesAndroidInjector(modules = {SplashActivityModule.class})
    @ActivityScope
    public abstract SplashActivity splashActivity();

    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    @ActivityScope
    public abstract LoginActivity loginActivity();

    @ContributesAndroidInjector(modules = {StartupActivityModule.class})
    @ActivityScope
    public abstract StartupActivity startupActivity();

    @ContributesAndroidInjector(modules = {UAEPassPinModule.class})
    @ActivityScope
    public abstract UAEPassPinActivity uaePassPinActivity();

    @ContributesAndroidInjector(modules = {PersonalDetailsModule.class})
    @ActivityScope
    public abstract PersonalDetailsActivity personalDetailsActivity();

    @ContributesAndroidInjector(modules = {ContactDetailsModule.class})
    @ActivityScope
    public abstract ContactDetailsActivity contactDetailsActivity();

    @ContributesAndroidInjector(modules = {SecurityQuestionsRoundOneModule.class})
    @ActivityScope
    public abstract SecurityQuestionsRoundOneActivity securityQuestionsRoundOneActivity();

    @ContributesAndroidInjector(modules = {SecurityQuestionsRoundTwoModule.class})
    @ActivityScope
    public abstract SecurityQuestionsRoundTwoActivity securityQuestionsRoundTwoActivity();

    @ContributesAndroidInjector(modules = {CreatePasswordModule.class})
    @ActivityScope
    public abstract CreatePasswordActivity createPasswordActivity();

    @ContributesAndroidInjector(modules = {ForgotPasswordModule.class})
    @ActivityScope
    public abstract ForgotPasswordActivity forgotPasswordActivity();

    @ContributesAndroidInjector(modules = {DashboardModule.class})
    @ActivityScope
    public abstract DashboardActivity dashboardActivity();

    @ContributesAndroidInjector(modules = {CheckoutModule.class})
    @ActivityScope
    public abstract CheckoutActivity checkoutActivity();

    @ContributesAndroidInjector(modules = {AddCardModule.class})
    @ActivityScope
    public abstract AddCardActivity addCardActivity();

    @ContributesAndroidInjector(modules = {AddRecipientsModule.class})
    @ActivityScope
    public abstract AddRecipientsActivity addRecipientsActivity();

    @ContributesAndroidInjector(modules = {CardListModule.class})
    @ActivityScope
    public abstract CardListActivity cardActivity();

    @ContributesAndroidInjector(modules = {PdfViewModule.class})
    @ActivityScope
    public abstract PdfViewActivity pdfViewActivity();

    @ContributesAndroidInjector(modules = {MenuActivityModule.class})
    @ActivityScope
    public abstract MenuActivity menuActivity();

    @ContributesAndroidInjector(modules = {ProfileActivityModule.class})
    @ActivityScope
    public abstract ProfileActivity profileActivity();

    @ContributesAndroidInjector(modules = {MaintenanceActivityModule.class})
    @ActivityScope
    public abstract MaintenanceActivity maintenanceActivity();

    @ContributesAndroidInjector(modules = {AboutUsModule.class})
    @ActivityScope
    public abstract AboutUsActivity aboutUsActivity();

    @ContributesAndroidInjector(modules = {ChangePasswordModule.class})
    @ActivityScope
    public abstract ChangePasswordActivity changePasswordActivity();

    @ContributesAndroidInjector(modules = {TermsAndConditionActivityModule.class})
    @ActivityScope
    public abstract TermsAndConditionActivity termsAndConditionActivity();

    @ContributesAndroidInjector(modules = {VerifyAmountModule.class})
    @ActivityScope
    public abstract VerifyAmountForPaymentActivity verifyAmountForPaymentActivity();

    @ContributesAndroidInjector(modules = {ScanEmirateIdModule.class})
    @ActivityScope
    public abstract ScanEmirateIdActivity scanEmirateIdActivity();

    @ContributesAndroidInjector(modules = {DeviceRootedModule.class})
    @ActivityScope
    public abstract DeviceRootedActivity deviceRootedActivity();

    @ContributesAndroidInjector(modules = {IntroMainModule.class})
    @ActivityScope
    public abstract IntroActivity introActivity();

    @ContributesAndroidInjector(modules = {EDirhamWebviewModule.class})
    @ActivityScope
    public abstract EDirhamWebviewActivity eDirhamWebviewActivity();

    @ContributesAndroidInjector(modules = {AdcbWebviewModule.class})
    @ActivityScope
    public abstract AdcbCardWebviewActivity adcbCardWebviewActivity();
}
