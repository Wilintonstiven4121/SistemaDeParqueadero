

package app;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Main {

    // =====================================================
    // COMMIT 1: Git con la creación del proyecto y main
    // =====================================================

    // Scanner global
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion;

        do {

            System.out.println("\n===== SISTEMA DE PARQUEADERO =====");
            System.out.println("1. Registrar entrada");
            System.out.println("2. Registrar salida");
            System.out.println("3. Reporte de ocupacion");
            System.out.println("4. Reporte financiero");
            System.out.println("5. Salir");

            opcion = leerEntero("Seleccione una opcion: ", 1, 5);

            switch (opcion) {

                case 1:
                    registroEntrada();
                    break;

                case 2:
                    registroSalida();
                    break;

                case 3:
                    reporteOcupacion();
                    break;

                case 4:
                    reporteFinanciero();
                    break;

                case 5:
                    System.out.println("Programa finalizado.");
                    break;
            }

        } while (opcion != 5);

        scanner.close();
    }

    // VALIDACION DE OPCIONES DEL MENU

    public static int leerEntero(String mensaje, int minimo, int maximo) {

        int numero;

        while (true) {

            System.out.print(mensaje);

            try {

                numero = Integer.parseInt(scanner.nextLine());

                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }

                System.out.println("Ingrese un valor entre "
                        + minimo + " y " + maximo + ".");

            } catch (NumberFormatException e) {

                System.out.println("Error: debe ingresar un numero entero.");

            }
        }
    }


    // METODOS INICIALES DEL MENU

    public static void registroEntrada() {
        System.out.println("\nModulo de registro de entrada.");
    }

    public static void registroSalida() {
        System.out.println("\nModulo de registro de salida.");
    }

    public static void reporteOcupacion() {
        System.out.println("\nModulo de reporte de ocupacion.");
    }

    public static void reporteFinanciero() {
        System.out.println("\nModulo de reporte financiero.");
    }



    // COMMIT 2: DECLARACION DE CONSTANTES, VARIABLES Y ARREGLOS INICIALES

    // Capacidad del parqueadero

    static final int CAPACIDAD_TOTAL = 10;

    // Tarifas para carros

    static final int TARIFA_HORA_CARRO = 4000;
    static final int TARIFA_DIA_CARRO = 30000;

    // Tarifas para motos

    static final int TARIFA_HORA_MOTO = 2000;
    static final int TARIFA_DIA_MOTO = 15000;

    // Horarios y condiciones

    static final int UMBRAL_HORAS_DIA = 8;
    static final int HORA_APERTURA = 8;
    static final int HORA_CIERRE = 22;

    // Formato de fecha y hora

    static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    // Arreglos para almacenar informacion de los vehiculos

    static String[] tiposVehiculo =
            new String[CAPACIDAD_TOTAL];

    static LocalDateTime[] fechasIngreso =
            new LocalDateTime[CAPACIDAD_TOTAL];

    // Variables de ocupacion

    static int celdasOcupadas = 0;
    static int celdasLibres = CAPACIDAD_TOTAL;

    // Variables de recaudo

    static long recaudoCarros = 0;
    static long recaudoMotos = 0;

}
