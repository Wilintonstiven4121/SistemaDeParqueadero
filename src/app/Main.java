
package app;

import java.util.Scanner;

public class Main {

    // Scanner global
    static Scanner scanner = new Scanner(System.in);

    // =====================================================
    // COMMIT 1: Git con la creación del proyecto y main
    // =====================================================

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

}
