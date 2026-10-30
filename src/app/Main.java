
package app;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    // COMMIT 1: CREACION DEL PROYECTO Y MAIN

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

    // COMMIT 2: CONSTANTES, VARIABLES Y ARREGLOS INICIALES

    static final int CAPACIDAD_TOTAL = 10;

    static final int TARIFA_HORA_CARRO = 4000;
    static final int TARIFA_DIA_CARRO = 30000;

    static final int TARIFA_HORA_MOTO = 2000;
    static final int TARIFA_DIA_MOTO = 15000;

    static final int UMBRAL_HORAS_DIA = 8;
    static final int HORA_APERTURA = 8;
    static final int HORA_CIERRE = 22;

    static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    static String[] tiposVehiculo =
            new String[CAPACIDAD_TOTAL];

    static LocalDateTime[] fechasIngreso =
            new LocalDateTime[CAPACIDAD_TOTAL];

    static int celdasOcupadas = 0;
    static int celdasLibres = CAPACIDAD_TOTAL;

    static long recaudoCarros = 0;
    static long recaudoMotos = 0;

    // COMMIT 3: REGISTRO DE ENTRADA

    public static void registroEntrada() {

        if (celdasLibres == 0) {

            System.out.println("El parqueadero esta lleno.");
            return;
        }

        System.out.println("\n--- REGISTRO DE ENTRADA ---");
        System.out.println("1. Carro");
        System.out.println("2. Moto");

        int opcion = leerEntero(
                "Seleccione el tipo de vehiculo: ", 1, 2);

        String tipo;

        if (opcion == 1) {
            tipo = "Carro";
        } else {
            tipo = "Moto";
        }

        LocalDateTime ingreso = leerFechaHora(
                "Fecha y hora de ingreso (yyyy-MM-dd HH:mm): ");

        int posicion = celdaDisponible();

        if (posicion == -1) {

            System.out.println("No hay celdas disponibles.");
            return;
        }

        tiposVehiculo[posicion] = tipo;
        fechasIngreso[posicion] = ingreso;

        celdasOcupadas++;
        celdasLibres--;

        System.out.println("\nEntrada registrada correctamente.");
        System.out.println("Celda asignada: " + (posicion + 1));
        System.out.println("Tipo de vehiculo: " + tipo);
        System.out.println("Fecha de ingreso: "
                + ingreso.format(FORMATO_FECHA));
    }

    // COMMIT 4: CELDAS DISPONIBLES

    public static int celdaDisponible() {

        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {

            if (tiposVehiculo[i] == null) {
                return i;
            }
        }

        return -1;
    }

    // COMMIT 5: VALIDACION DE DATOS

    public static int leerEntero(
            String mensaje, int minimo, int maximo) {

        while (true) {

            System.out.print(mensaje);

            String texto = scanner.nextLine().trim();

            try {

                int numero = Integer.parseInt(texto);

                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }

            } catch (NumberFormatException e) {

            }

            System.out.println(
                    "Dato invalido. Ingrese un numero entre "
                            + minimo + " y " + maximo + ".");
        }
    }

    public static LocalDateTime leerFechaHora(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            String texto = scanner.nextLine().trim();

            try {

                LocalDateTime fecha = LocalDateTime.parse(
                        texto, FORMATO_FECHA);

                LocalTime hora = fecha.toLocalTime();

                if (hora.isBefore(LocalTime.of(HORA_APERTURA, 0))
                        || hora.isAfter(LocalTime.of(HORA_CIERRE, 0))) {

                    System.out.println(
                            "El horario permitido es de 08:00 a 22:00.");

                    continue;
                }

                return fecha;

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Fecha invalida. Use yyyy-MM-dd HH:mm.");
            }
        }
    }

    // COMMIT 6: REGISTRO DE SALIDA

    public static void registroSalida() {

        System.out.println("\n--- REGISTRO DE SALIDA ---");

        int celda = leerEntero(
                "Numero de celda (1 a 10): ",
                1, CAPACIDAD_TOTAL);

        int indice = celda - 1;

        if (tiposVehiculo[indice] == null) {

            System.out.println("La celda esta vacia.");
            return;
        }

        LocalDateTime ingreso = fechasIngreso[indice];

        System.out.println("Vehiculo: " + tiposVehiculo[indice]);
        System.out.println("Fecha de ingreso: "
                + ingreso.format(FORMATO_FECHA));

        LocalDateTime salida;

        while (true) {

            salida = leerFechaHora(
                    "Fecha y hora de salida (yyyy-MM-dd HH:mm): ");

            if (!salida.isBefore(ingreso)) {
                break;
            }

            System.out.println(
                    "La salida no puede ser anterior al ingreso.");
        }


        liquidacion(
                celda,
                ingreso,
                salida,
                tiposVehiculo[indice]);

        tiposVehiculo[indice] = null;
        fechasIngreso[indice] = null;

        celdasOcupadas--;
        celdasLibres++;

        System.out.println(
                "La celda " + celda + " esta disponible.");
    }

    // LIQUIDACION Y CALCULO DEL COBRO

    public static void liquidacion(
            int celda,
            LocalDateTime ingreso,
            LocalDateTime salida,
            String tipo) {

        long minutos = ChronoUnit.MINUTES.between(
                ingreso, salida);

        long horas = Math.max(1, (minutos + 59) / 60);

        long tarifaHora;
        long tarifaDia;

        if (tipo.equals("Carro")) {

            tarifaHora = TARIFA_HORA_CARRO;
            tarifaDia = TARIFA_DIA_CARRO;

        } else {

            tarifaHora = TARIFA_HORA_MOTO;
            tarifaDia = TARIFA_DIA_MOTO;
        }

        long total;

        if (horas < UMBRAL_HORAS_DIA) {

            total = horas * tarifaHora;

        } else {

            long dias = (horas + 23) / 24;

            total = dias * tarifaDia;
        }

        if (tipo.equals("Carro")) {

            recaudoCarros += total;

        } else {

            recaudoMotos += total;
        }

        System.out.println("\n===== LIQUIDACION =====");

        System.out.println("Celda: " + celda);
        System.out.println("Vehiculo: " + tipo);

        System.out.println("Ingreso: "
                + ingreso.format(FORMATO_FECHA));

        System.out.println("Salida: "
                + salida.format(FORMATO_FECHA));

        System.out.println("Tiempo de permanencia: "
                + horas + " horas cobradas");

        System.out.println("Valor a pagar: $"
                + total);

        System.out.println("Pago registrado correctamente.");
    }

    public static void reporteOcupacion() {

        System.out.println("\nModulo de reporte de ocupacion.");
    }

    public static void reporteFinanciero() {

        System.out.println("\nModulo de reporte financiero.");
    }

}
