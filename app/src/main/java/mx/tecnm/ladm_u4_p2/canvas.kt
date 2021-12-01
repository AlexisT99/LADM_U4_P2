package mx.tecnm.ladm_u4_p2

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.abs

class canvas  (p:MainActivity, d: SensorManager, v: SensorManager): View(p), SensorEventListener {
    var noche = BitmapFactory.decodeResource(resources,R.drawable.noche)
    var dia = BitmapFactory.decodeResource(resources,R.drawable.dia)
    var paint1 = pintura(0,0,noche)
    var paint2 = pintura(0,0,dia)
    var listenerAce = d
    var listenerProx = v
    var esDia = false
    val p = p
    var anterior:Float = 0.0F
    var seUsoAcel = false
    var nuevosCopos = ArrayList<Copo>()
    val nieve = ArrayList<Copo>()
    val movimientoNieve = MovimientoNieve(this)
    init {
        movimientoNieve.start()
        val builder = AlertDialog.Builder(p)
        builder.setTitle("Advertencia")
        builder.setMessage("Usa el sensor de proximidad y el acelerometro(en este haz movimientos bruscos para que se vea :D)")
        builder.setPositiveButton("OK"){ d,w->
            d.dismiss()
        }
        builder.show()
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val paint = Paint()

        listenerAce.registerListener(this,listenerAce.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
        listenerProx.registerListener(this,listenerProx.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL)
        paint2.pintar(c,0f,0f,paint)
        paint1.pintar(c,0f,0f,paint)

        if(!esDia) {
            c.drawBitmap(noche, 0f, 0f, paint)
        }
        else {
            c.drawBitmap(dia, 0f, 0f, paint)
        }
        paint.color = Color.WHITE
        nieve.forEach {
            c.drawCircle(it.x,it.y,it.tam,paint)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(Sensor.TYPE_ACCELEROMETER == event!!.sensor.type){
            if(abs(event.values[0]-anterior) >2){
                seUsoAcel = true
            }
           anterior = event.values[0]
        }
        if(Sensor.TYPE_PROXIMITY == event.sensor.type){
            esDia = event.values[0] >= 2
            invalidate()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}

class MovimientoNieve(p:canvas):Thread(){
    private val puntero = p

    @RequiresApi(Build.VERSION_CODES.N)
    override fun run() {
        super.run()
        while (true){
            puntero.nieve.forEach {
                it.moverCopo()
            }
            if(puntero.seUsoAcel){
                (1..15).forEach { _ ->
                    val copo = Copo()
                    puntero.nuevosCopos.add(copo)
                }
                puntero.seUsoAcel = false
            }
            puntero.nieve.addAll(puntero.nuevosCopos)
            puntero.nuevosCopos.clear()
            puntero.nieve.removeIf { a -> !a.vivo }
            puntero.p.runOnUiThread() {
                puntero.invalidate()
            }
            sleep(80)
        }
    }
}