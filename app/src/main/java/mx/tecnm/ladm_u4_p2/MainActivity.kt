package mx.tecnm.ladm_u4_p2

import android.content.Context
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.tecnm.ladm_u4_p2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var b:ActivityMainBinding
    lateinit var sensorManagerMov : SensorManager
    lateinit var sensorManagerProx : SensorManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        sensorManagerMov = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManagerProx = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        setContentView(canvas(this,sensorManagerMov,sensorManagerProx))
    }
}