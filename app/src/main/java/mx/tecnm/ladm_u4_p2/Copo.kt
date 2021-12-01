package mx.tecnm.ladm_u4_p2

class Copo {
    var x= 0f
    var y = 0f
    var tam = 0f
    var vivo = true

    init {
        x= (Math.random()*1800).toFloat()
        y = ((Math.random()*-300)).toFloat() //2340
        tam = ((Math.random()*5)+5).toFloat()
        vivo = true
    }

    fun moverCopo(){
        y += ((Math.random() * 4) + 10).toFloat()
        if(y>=2200){
            vivo = false
        }
    }
}