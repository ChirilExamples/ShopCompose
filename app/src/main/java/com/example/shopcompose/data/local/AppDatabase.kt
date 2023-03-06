//package com.example.shopcompose.data.local
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.util.Log
//import androidx.core.content.edit
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.shopcompose.data.data_structure.ClothesItem
//import javax.crypto.KeyGenerator
//
//const val DATABASE_NAME = "SHOP.DB"
//const val PREF_KEY_PASS = "SHOP"
//
//const val SHARED_PREF_NAME = "ShopKey"
//
//const val ALGORITHM_AES = "AES"
//const val KEY_SIZE = 256
//
//@Database(entities = [ClothesItem::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun shoppingDAO(): ShoppingDAO
//
//    companion object {
//
//        private fun generatePass(): ByteArray {
//            val keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES)
//            keyGenerator.init(KEY_SIZE)
//            Log.i("Pass", keyGenerator.generateKey().encoded.toString())
//            return keyGenerator.generateKey().encoded
//        }
//
//        /**
//         * Generates a passphrase and stores it in the encrypted shared preferences.
//         * Returns the newly generated passphrase.
//         */
//
//        private fun initializePassphrase(context: Context): ByteArray {
//            val passphrase = generatePass()
//
//            getSharedPrefs(context).edit(commit = true) {
//                putString(PREF_KEY_PASS, passphrase.toString(Charsets.ISO_8859_1))
//            }
//
//            return passphrase
//        }
//
//        /**
//         * Retrieves the passphrase for encryption from the encrypted shared preferences.
//         * Returns null if there is no stored passphrase.
//         */
//        private fun getPassphrase(context: Context): ByteArray? {
//            val passphraseString = getSharedPrefs(context)
//                .getString(PREF_KEY_PASS, null)
//            return passphraseString?.toByteArray(Charsets.ISO_8859_1)
//        }
//
//        /**
//         * Returns a reference to the encrypted shared preferences.
//         */
//        private fun getSharedPrefs(context: Context): SharedPreferences {
//            val masterKey =
//                MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
//                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//                    .build()
//
//            return EncryptedSharedPreferences.create(
//                context,
//                SHARED_PREF_NAME,
//                masterKey,
//                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            )
//        }
//
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
//            }
//
//        private fun buildDatabase(context: Context): AppDatabase {
//            val passphrase = getPassphrase(context) ?: initializePassphrase(context)
//            val factory = SupportFactory(passphrase)
//
//            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//                .openHelperFactory(factory)
//                .fallbackToDestructiveMigration()
//                .build()
//        }
//    }
//}
