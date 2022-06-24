package com.aecb.di.modules.app;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aecb.AppConstants;
import com.aecb.data.db.MyAppDb;
import com.aecb.data.db.repository.commonresponse.ResponseRepo;
import com.aecb.data.db.repository.commonresponse.ResponseRepoImpl;
import com.aecb.data.db.repository.notification.NotificationImpl;
import com.aecb.data.db.repository.notification.NotificationRepo;
import com.aecb.data.db.repository.userInformation.UserImpl;
import com.aecb.data.db.repository.userInformation.UserRepo;
import com.aecb.data.db.repository.usertcversion.UserTCImpl;
import com.aecb.data.db.repository.usertcversion.UserTCRepo;
import com.aecb.di.qualifiers.AppContext;
import com.aecb.di.scopes.AppScope;
import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;

import static com.aecb.util.Utilities.dFullString;
import static com.commonsware.cwac.saferoom.SQLCipherUtils.State.UNENCRYPTED;

@Module(includes = {AppContextModule.class})
public class DbModule {

    @Provides
    @AppScope
    public MyAppDb myAppDb(@AppContext Context context) {
        byte[] encryptionKey = dFullString().getBytes();
        SafeHelperFactory factory = new SafeHelperFactory(encryptionKey);
        SQLCipherUtils.State state = SQLCipherUtils.getDatabaseState(context, AppConstants.DB_NAME);
        if (state.equals(UNENCRYPTED)) {
            try {
                SQLCipherUtils.encrypt(context, AppConstants.DB_NAME, encryptionKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Room.databaseBuilder(context, MyAppDb.class, AppConstants.DB_NAME)
                .openHelperFactory(factory)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();
    }

    final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE DBUserTC ADD COLUMN preferredPaymentMethod TEXT");
        }
    };

    final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE DBUserTC ADD COLUMN isUAEPassUser INTEGER NOT NULL DEFAULT 0");
        }
    };

    @Provides
    @AppScope
    public UserRepo provideUserRepoHelper(MyAppDb myAppDb) {
        return (new UserImpl(myAppDb.userDao()));
    }

    @Provides
    @AppScope
    public UserTCRepo provideUserTCRepoHelper(MyAppDb myAppDb) {
        return (new UserTCImpl(myAppDb.userTCDao()));
    }

    @Provides
    @AppScope
    public ResponseRepo provideResponseHelper(MyAppDb myAppDb) {
        return (new ResponseRepoImpl(myAppDb.responseDao()));
    }

    @Provides
    @AppScope
    public NotificationRepo provideNotificationHelper(MyAppDb myAppDb) {
        return (new NotificationImpl(myAppDb.notificationDao()));
    }

}
