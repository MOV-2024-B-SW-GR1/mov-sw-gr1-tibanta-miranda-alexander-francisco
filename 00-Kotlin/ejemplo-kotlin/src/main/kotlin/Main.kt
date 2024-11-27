package ec.edu.epn

import java.util.*

fun main() {
    println("Hello World!")

    // INMUTABLES (No se RE ASIGNA "=")
    val inmutable: String = "Alexander Tibanta"
    // inmutable = "Vicente" // Error!

    // MUTABLES
    var mutable: String = "Alexander Tibanta"
    mutable = "Alexander Tibanta" // Ok

    // VAL > VAR

    //---- Duck Typing + Variables------------
    val ejemploVariable = "Alexander Tibanta"
    ejemploVariable.trim()
    val edadEjemplo: Int = 22
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 5.6
    val estadoCivil: Char = 'S'
    val mayorEdad: Boolean = true

    // Clases en Java
    val fechaNacimiento: Date = Date()

    // When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }
    // -------- if - else ------------
    val esSoltero = (estadoCivilWhen == "S")
    val coqueto = if (esSoltero) "Si" else "No" // if else
    // -------------- LLAMADA DE FUNCIONES ---------------
    imprimirNombre(ejemploVariable)
    calcularSueldo(10.00) //solo parametro requerido
    calcularSueldo(10.00, 15.00, 20.00) //parametro requerido y sobreescribir parametros opcionales
    //Named parameters
    calcularSueldo(10.00, bonoEspecial = 20.00) // usando el parametro bonoEspecial en la segunda posicion
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)
    // usando el parametro bonoEspecial en primera posicion
    // usando el parametro sueldo en la segunda posicion
    // usando el parametro tasa es tercera posicion
    // gracias a los parametros nombrados


    // -------------------CLASES USO-----------------------
    //4 instancias usando todos los constructores
    val sumaA = Suma(1,1)
    val sumaB = Suma(null,1)
    val sumaC = Suma(1,null)
    val sumaD = Suma(null,null)


    //Usamos la función sumar dentro de cada instancia
    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()

    //Uso de component object como static
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)
}

//-------------FUNCIONES----------
fun imprimirNombre(nombre:String):Unit{
    fun otraFuncionAdentro(){
        print("Otra funcion adentro")
    }
    println("Nombre: $nombre") //Template Strings
    println("Nombre: ${nombre}") //Template Strings
    println("Nombre: ${nombre + nombre}") //Uso con llaves (concatenado)
    println("Nombre: ${nombre.uppercase()}") //Uso con llaves (funcion)
    println("Nombre: $nombre.uppercase()") //INCORRECTO!
    //No puedo usar sin llaves
}
fun calcularSueldo(
    sueldo: Double, //Requerido
    tasa: Double = 12.00, //Opcional (defecto)
    bonoEspecial: Double? = null //Opcional (nullable)
    // Variable? - "?" Es Nullable (quiere decir que algun momento puede se nulo)
): Double{
    // Into -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)
    return if(bonoEspecial == null){
        sueldo * (100/tasa)
    }else{
        sueldo * (100/tasa) * bonoEspecial
    }
}

//------------ CLASES --------------
//Clase normal de java
abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
    }
}

//-------Kotlin Classes
//Clase Padre
abstract class Numeros( //Constructor Primario
    //Caso 1) Parametro normal
    //uno:Int, (parametro (sin modificador acceso))
    //Caso 2) Parámetro y propiedad (atributo) (protected)
    // private var uno: Int (propiedad "instancia.uno")
    protected val numeroUno: Int,
    protected val numeroDos: Int,
    parámetroNoUsadoNoPropiedadDeLaClase: Int? = null
){
    init {
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

// Clase hija llamada "Suma", que hereda de la clase padre "Numeros".
class Suma(
    // Constructor primario: Recibe dos parámetros enteros.
    unoParametro: Int,
    dosParametro: Int,
) : Numeros( // Llamada al constructor de la clase padre "Numeros".
    unoParametro, // Se pasa el primer parámetro al constructor de "Numeros".
    dosParametro  // Se pasa el segundo parámetro al constructor de "Numeros".
) {
    // --------- Propiedades de la Clase ---------
    // Modificadores de acceso:
    public val soyPublicoExplicito: String = "Publicas" // Propiedad explícitamente pública.
    val soyPublicoImplicito: String = "Publico implicito" // Propiedad implícitamente pública (por defecto en Kotlin).

    // Bloque de inicialización: Se ejecuta al crear una instancia de la clase.
    init {
        // Accediendo a las propiedades heredadas de la clase padre "Numeros".
        this.numeroUno // Uso de `this` para hacer referencia a las propiedades heredadas (opcional).
        this.numeroDos
        numeroUno // Se puede omitir `this` al referirse a propiedades heredadas o de la clase.
        numeroDos
        this.soyPublicoExplicito // Accediendo a una propiedad explícitamente pública.
        soyPublicoImplicito // Accediendo a una propiedad pública implícita.
    }

    // --------- Constructores Secundarios ---------
    // Estos permiten instanciar la clase con diferentes tipos de parámetros.

    // Constructor secundario 1: Recibe un número nullable (puede ser null) y un entero.
    constructor(
        uno: Int?, // Primer parámetro nullable.
        dos: Int   // Segundo parámetro no nullable.
    ) : this(
        if (uno == null) 0 else uno, // Si `uno` es null, se asigna 0. Caso contrario, se usa su valor.
        dos
    ) {
        // Este bloque es opcional, se ejecuta al usar este constructor.
    }

    // Constructor secundario 2: Recibe un entero y un número nullable.
    constructor(
        uno: Int,  // Primer parámetro no nullable.
        dos: Int?  // Segundo parámetro nullable.
    ) : this(
        uno,
        if (dos == null) 0 else dos // Si `dos` es null, se asigna 0.
    )

    // Constructor secundario 3: Ambos parámetros son nullable.
    constructor(
        uno: Int?, // Primer parámetro nullable.
        dos: Int?  // Segundo parámetro nullable.
    ) : this(
        if (uno == null) 0 else uno, // Si `uno` es null, se asigna 0.
        if (dos == null) 0 else dos // Si `dos` es null, se asigna 0.
    )

    // --------- Métodos ---------
    // Método que suma las propiedades heredadas `numeroUno` y `numeroDos`.
    fun sumar(): Int {
        val total = numeroUno + numeroDos // Realiza la suma.
        agregarHistorial(total) // Guarda el resultado en el historial.
        return total // Devuelve el resultado.
    }

    // Companion Object: Se comparte entre todas las instancias de la clase.
    companion object {
        // Propiedades y métodos estáticos equivalentes a "static" en Java.

        val pi = 3.14 // Constante compartida entre instancias.

        // Método para elevar un número al cuadrado.
        fun elevarAlCuadrado(num: Int): Int {
            return num * num
        }

        // Historial compartido para registrar los resultados de las sumas.
        val historialSumas = arrayListOf<Int>()

        // Método para agregar un resultado al historial.
        fun agregarHistorial(valorTotalSuma: Int) {
            historialSumas.add(valorTotalSuma)
        }
    }
}
