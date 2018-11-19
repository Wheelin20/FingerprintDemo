package com.waldoms.fingerprintdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.app.KeyguardManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.biometrics.BiometricPrompt
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import javax.crypto.KeyGenerator

class FingerprintDemoActivity : AppCompatActivity()
{
    private var keyguardManager:KeyguardManager? = null
    private var fingerprintManager:FingerprintManager? = null
    private var keyStore:KeyStore? = null
    private var keyGenerator:KeyGenerator? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_demo)

        if(getManagers())
        {

        }
    }

    private fun getManagers():Boolean
    {
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

        if(keyguardManager?.isKeyguardSecure == false)
        {
            Toast.makeText(this, "Lock screen security not enabled in Settings", Toast.LENGTH_LONG).show()

            return false
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Fingerprint authentication permission not enabled", Toast.LENGTH_LONG).show()

            return false
        }

        if(fingerprintManager?.hasEnrolledFingerprints() == false)
        {
            Toast.makeText(this, "Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show()

            return false
        }

        return true
    }

    private fun generateKey()
    {
        try
        {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        }
        catch(e:Exception) { e.printStackTrace() }

        try
        {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        }
        catch(e:NoSuchAlgorithmException) { throw RuntimeException("Failed to get KeyGenerator instance", e) }
        catch(e:NoSuchProviderException) { throw RuntimeException("Failed to get KeyGenerator instance", e) }
    }
}
