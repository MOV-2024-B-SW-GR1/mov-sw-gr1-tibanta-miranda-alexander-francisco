package ec.edu.epn

import java.io.*
import java.text.SimpleDateFormat
import java.util.*

data class Empresa(
    val id: Int,
    var nombre: String,
    var anioFundacion: Int,
    var activa: Boolean,
    var ingresosAnuales: Double,
    val empleados: MutableList<Empleado> = mutableListOf()
)

data class Empleado(
    val id: Int,
    var nombre: String,
    var edad: Int,
    var cargo: String,
    var fechaContratacion: Date,
    var salarioMensual: Double
)

val empresas = mutableListOf<Empresa>()
val dateFormat = SimpleDateFormat("yyyy-MM-dd")
const val EMPRESA_FILE = "empresas.txt"
const val EMPLEADO_FILE = "empleados.txt"

fun main() {
    cargarDatos()
    while (true) {
        println("\n=== CRUD Empresas y Empleados ===")
        println("1. Crear Empresa")
        println("2. Listar Empresas")
        println("3. Actualizar Empresa")
        println("4. Eliminar Empresa")
        println("5. Administrar Empleados de Empresa")
        println("6. Salir")
        print("Seleccione una opción: ")
        when (readLine()!!.toInt()) {
            1 -> crearEmpresa()
            2 -> listarEmpresas()
            3 -> actualizarEmpresa()
            4 -> eliminarEmpresa()
            5 -> administrarEmpleados()
            6 -> {
                guardarDatos()
                println("¡Hasta luego!")
                return
            }
            else -> println("Opción inválida. Intente nuevamente.")
        }
    }
}

fun cargarDatos() {
    // Leer datos de empresas
    File(EMPRESA_FILE).takeIf { it.exists() }?.forEachLine { line ->
        val data = line.split("|")
        val empleados = mutableListOf<Empleado>()
        empresas.add(
            Empresa(
                data[0].toInt(), data[1], data[2].toInt(), data[3].toBoolean(), data[4].toDouble(), empleados
            )
        )
    }

    // Leer datos de empleados
    File(EMPLEADO_FILE).takeIf { it.exists() }?.forEachLine { line ->
        val data = line.split("|")
        val empresaId = data[5].toInt()
        val empresa = empresas.find { it.id == empresaId }
        empresa?.empleados?.add(
            Empleado(
                data[0].toInt(), data[1], data[2].toInt(), data[3],
                dateFormat.parse(data[4]), data[5].toDouble()
            )
        )
    }
}

fun guardarDatos() {
    // Guardar empresas
    File(EMPRESA_FILE).bufferedWriter().use { out ->
        empresas.forEach { empresa ->
            out.write("${empresa.id}|${empresa.nombre}|${empresa.anioFundacion}|${empresa.activa}|${empresa.ingresosAnuales}\n")
        }
    }

    // Guardar empleados
    File(EMPLEADO_FILE).bufferedWriter().use { out ->
        empresas.forEach { empresa ->
            empresa.empleados.forEach { empleado ->
                out.write("${empleado.id}|${empleado.nombre}|${empleado.edad}|${empleado.cargo}|${dateFormat.format(empleado.fechaContratacion)}|${empleado.salarioMensual}|${empresa.id}\n")
            }
        }
    }
}

fun crearEmpresa() {
    println("\n=== Crear Empresa ===")
    print("Nombre: ")
    val nombre = readLine()!!
    print("Año de Fundación: ")
    val anio = readLine()!!.toInt()
    print("¿Está activa? (true/false): ")
    val activa = readLine()!!.toBoolean()
    print("Ingresos Anuales: ")
    val ingresos = readLine()!!.toDouble()

    val id = (empresas.maxOfOrNull { it.id } ?: 0) + 1
    empresas.add(Empresa(id, nombre, anio, activa, ingresos))
    println("Empresa creada exitosamente.")
}

fun listarEmpresas() {
    println("\n=== Listar Empresas ===")
    empresas.forEach { empresa ->
        println("${empresa.id}. ${empresa.nombre} - Año: ${empresa.anioFundacion} - Activa: ${empresa.activa} - Ingresos: ${empresa.ingresosAnuales} - Empleados: ${empresa.empleados.size}")
    }
}

fun actualizarEmpresa() {
    println("\n=== Actualizar Empresa ===")
    listarEmpresas()
    print("ID de la Empresa a actualizar: ")
    val id = readLine()!!.toInt()
    val empresa = empresas.find { it.id == id }
    if (empresa != null) {
        print("Nuevo Nombre (${empresa.nombre}): ")
        empresa.nombre = readLine()!!
        print("Nuevo Año de Fundación (${empresa.anioFundacion}): ")
        empresa.anioFundacion = readLine()!!.toInt()
        print("¿Está activa? (${empresa.activa}): ")
        empresa.activa = readLine()!!.toBoolean()
        print("Nuevos Ingresos Anuales (${empresa.ingresosAnuales}): ")
        empresa.ingresosAnuales = readLine()!!.toDouble()
        println("Empresa actualizada exitosamente.")
    } else {
        println("Empresa no encontrada.")
    }
}

fun eliminarEmpresa() {
    println("\n=== Eliminar Empresa ===")
    listarEmpresas()
    print("ID de la Empresa a eliminar: ")
    val id = readLine()!!.toInt()
    if (empresas.removeIf { it.id == id }) {
        println("Empresa eliminada exitosamente.")
    } else {
        println("Empresa no encontrada.")
    }
}

fun administrarEmpleados() {
    println("\n=== Administrar Empleados ===")
    listarEmpresas()
    print("ID de la Empresa: ")
    val empresaId = readLine()!!.toInt()
    val empresa = empresas.find { it.id == empresaId }
    if (empresa != null) {
        while (true) {
            println("\n=== Empleados de ${empresa.nombre} ===")
            println("1. Añadir Empleado")
            println("2. Listar Empleados")
            println("3. Actualizar Empleado")
            println("4. Eliminar Empleado")
            println("5. Volver")
            print("Seleccione una opción: ")
            when (readLine()!!.toInt()) {
                1 -> añadirEmpleado(empresa)
                2 -> listarEmpleados(empresa)
                3 -> actualizarEmpleado(empresa)
                4 -> eliminarEmpleado(empresa)
                5 -> return
                else -> println("Opción inválida.")
            }
        }
    } else {
        println("Empresa no encontrada.")
    }
}

fun añadirEmpleado(empresa: Empresa) {
    println("\n=== Añadir Empleado ===")
    val nombreRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+\$")

    print("Nombre (solo letras y espacios): ")
    val nombre = readln()
    if (!nombreRegex.matches(nombre)) {
        println("Nombre inválido. Intente de nuevo.")
        return
    }

    print("Edad (número entero): ")
    val edad = readln().toIntOrNull()
    if (edad == null || edad < 18) {
        println("Edad inválida. Debe ser un número entero mayor o igual a 18.")
        return
    }

    print("Cargo (solo letras y espacios): ")
    val cargo = readln()
    if (!nombreRegex.matches(cargo)) {
        println("Cargo inválido. Intente de nuevo.")
        return
    }

    print("Fecha de Contratación (yyyy-MM-dd): ")
    val fechaInput = readln()
    val fechaContratacion = try {
        dateFormat.parse(fechaInput)
    } catch (e: Exception) {
        println("Fecha inválida. Formato esperado: yyyy-MM-dd.")
        return
    }

    print("Salario Mensual (número decimal): ")
    val salario = readln().toDoubleOrNull()
    if (salario == null || salario < 0) {
        println("Salario inválido. Debe ser un número positivo.")
        return
    }

    val id = (empresa.empleados.maxOfOrNull { it.id } ?: 0) + 1
    empresa.empleados.add(Empleado(id, nombre, edad, cargo, fechaContratacion, salario))
    println("Empleado añadido exitosamente.")
}

fun listarEmpleados(empresa: Empresa) {
    println("\n=== Listar Empleados de ${empresa.nombre} ===")
    empresa.empleados.forEach { empleado ->
        println("${empleado.id}. ${empleado.nombre} - Edad: ${empleado.edad} - Cargo: ${empleado.cargo} - Fecha: ${dateFormat.format(empleado.fechaContratacion)} - Salario: ${empleado.salarioMensual}")
    }
}

fun actualizarEmpleado(empresa: Empresa) {
    println("\n=== Actualizar Empleado ===")
    listarEmpleados(empresa)
    print("ID del Empleado a actualizar: ")
    val id = readLine()!!.toInt()
    val empleado = empresa.empleados.find { it.id == id }
    if (empleado != null) {
        print("Nuevo Nombre (${empleado.nombre}): ")
        empleado.nombre = readLine()!!
        print("Nueva Edad (${empleado.edad}): ")
        empleado.edad = readLine()!!.toInt()
        print("Nuevo Cargo (${empleado.cargo}): ")
        empleado.cargo = readLine()!!
        print("Nueva Fecha de Contratación (${dateFormat.format(empleado.fechaContratacion)}): ")
        empleado.fechaContratacion = dateFormat.parse(readLine()!!)
        print("Nuevo Salario Mensual (${empleado.salarioMensual}): ")
        empleado.salarioMensual = readLine()!!.toDouble()
        println("Empleado actualizado exitosamente.")
    } else {
        println("Empleado no encontrado.")
    }
}

fun eliminarEmpleado(empresa: Empresa) {
    println("\n=== Eliminar Empleado ===")
    listarEmpleados(empresa)
    print("ID del Empleado a eliminar: ")
    val id = readLine()!!.toInt()
    if (empresa.empleados.removeIf { it.id == id }) {
        println("Empleado eliminado exitosamente.")
    } else {
        println("Empleado no encontrado.")
    }
}