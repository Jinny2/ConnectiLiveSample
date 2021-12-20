package com.kakaovx.practice.connectilivesample.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kakaovx.practice.connectilivesample.R
import com.kakaovx.practice.connectilivesample.databinding.ActivityMainBinding
import com.remotemonster.sdk.Config
import com.remotemonster.sdk.RemonCast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var testChannelId: String = ""

    private val config: Config by lazy {
        Config().apply {
            serviceId = this@MainActivity.getString(R.string.service_id)
            key = this@MainActivity.getString(R.string.service_key)
            logLevel = Log.VERBOSE // SILENT, ERROR, WARN, INFO, DEBUG, VERBOSE 순으로 자세함.
            remoteView = binding.remoteVideoView
        }
    }

    private val viewer: RemonCast by lazy {
        RemonCast.builder()
            .context(this)
            .remoteView(config.remoteView)
            .serviceId(config.serviceId)
            .key(config.key)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
    }

    private fun checkPermission() {
        // Camera, Record_Audio, Write_External_Storage
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("THEEND", "권한 o")
            watchBroadCast()
            setBroadCastCallback()

            searchChannelList()
            closeBroadCast()
        } else {
            Log.d("THEEND", "권한 x")
            requestPermission()
        }
    }

    private fun requestPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.CAMERA)) {
//            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), 1)
//            Log.d("THEEND", "should grant 1-1")
//        } else {
//            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), 1)
//            Log.d("THEEND", "should grant 1-2")
//        }
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.RECORD_AUDIO)) {
//            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.RECORD_AUDIO), 2)
//            Log.d("THEEND", "should grant 2-1")
//        } else {
//            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.RECORD_AUDIO), 2)
//            Log.d("THEEND", "should grant 2-2")
//        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 4)
            Log.d("THEEND", "should grant 4-1")
        } else {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 4)
            Log.d("THEEND", "should grant 4-2")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty()) return

        Log.d("THEEND", "Call onRequestPermissionsResult: " + requestCode + ", " + permissions + ", " + grantResults)
        when (requestCode) {
            1 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("THEEND", "CAMERA GRANT")
            }
            2 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("THEEND", "RECORD_AUDIO GRANT")
            }
            4 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("THEEND", "WRITE_EXTERNAL_STORAGE GRANT")
            }
        }
    }

    // 방송 시청하기
    private fun watchBroadCast() {
        viewer.onJoin {
            // ...
            Log.d("THEEND", "watchBroadCast's onJoin")
        }
        binding.btnJoin.setOnClickListener {
            Log.d("THEEND", "testChannelId: " + testChannelId)
            viewer.join(testChannelId) // 들어가고자 하는 Channel
        }
    }

    // Channel 목록 조회
    // (방송을 만들면 채널이 생성되고 고유한 ChannelId가 생성된다.)
    private fun searchChannelList() {
        binding.getChannelList.setOnClickListener {
            viewer.fetchCasts()
        }

        viewer.onFetch { casts ->
            Log.d("THEEND", "onFetch: " + casts)
            for (cast in casts) {
                testChannelId = cast.id
            }
        }
    }

    // 방송 송출/시청이 끝났을 경우 종료해주기
    private fun closeBroadCast() {
        binding.btnClose.setOnClickListener {
            viewer.close()
        }

        // 종료
        viewer.onClose {
            Log.d("THEEND", "onClose")
        }
    }

    private fun setBroadCastCallback() {
        // UI 처리 등 Remon이 초기화 되었을 떄 처리하여야 할 작업
        viewer.onInit {
            Log.d("THEEND", "onInit")
        }

        // Caller, Callee 간 통화 시작
        viewer.onComplete {
            Log.d("THEEND", "onComplete")
        }

        viewer.onError {
            Log.d("THEEND", "onError: " + it.remonError)
            Log.d("THEEND", "onError: " + it.remonErrorLog)
            Log.d("THEEND", "onError: " + it.description)
        }
    }
}