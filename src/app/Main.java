
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

    static void registroEntrada() {

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

    static int celdaDisponible() {

        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {

            if (tiposVehiculo[i] == null) {
                return i;
            }
        }

        return -1;
    }

    // COMMIT 5: VALIDACION DE DATOS

    static int leerEntero(
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

    static LocalDateTime leerFechaHora(String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            try {
                LocalDateTime fecha = LocalDateTime.parse(
                        texto, FORMATO_FECHA);

                if (!fecha.format(FORMATO_FECHA).equals(texto)) {
                    System.out.println("Formato de fecha incorrecto.");
                    continue;
                }

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

    static void registroSalida() {

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

            if (salida.isAfter(ingreso)) {
                break;
            }

            System.out.println(
                    "La salida debe ser posterior al ingreso.");
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

    // COMMIT 7: LIQUIDACION

    static void liquidacion(
            int celda,
            LocalDateTime ingreso,
            LocalDateTime salida,
            String tipo) {

        long dias = ChronoUnit.DAYS.between(ingreso, salida);

        LocalDateTime cursor = ingreso.plusDays(dias);

        long minutosRestantes =
                ChronoUnit.MINUTES.between(cursor, salida);

        long horas = (minutosRestantes + 59) / 60;

        long tarifaHora;
        long tarifaDia;

        if (tipo.equals("Carro")) {
            tarifaHora = TARIFA_HORA_CARRO;
            tarifaDia = TARIFA_DIA_CARRO;
        } else {
            tarifaHora = TARIFA_HORA_MOTO;
            tarifaDia = TARIFA_DIA_MOTO;
        }

        if (horas >= UMBRAL_HORAS_DIA) {
            dias++;
            horas = 0;
        }

        long valorDias = dias * tarifaDia;
        long valorHoras = horas * tarifaHora;

        long total = valorDias + valorHoras;

        if (tipo.equals("Carro")) {
            recaudoCarros += total;
        } else {
            recaudoMotos += total;
        }

        System.out.println("\n========== FACTURA ==========");
        System.out.println("Celda: " + celda);
        System.out.println("Tipo de vehiculo: " + tipo);

        System.out.println("Ingreso: "
                + ingreso.format(FORMATO_FECHA));

        System.out.println("Salida: "
                + salida.format(FORMATO_FECHA));

        System.out.println("Dias cobrados: " + dias);
        System.out.println("Valor por dias: $" + valorDias);

        System.out.println("Horas cobradas: " + horas);
        System.out.println("Valor por horas: $" + valorHoras);

        System.out.println("-----------------------------");
        System.out.println("TOTAL A PAGAR: $" + total);
        System.out.println("=============================");

        System.out.println("Pago registrado correctamente.");
    }

    // COMMIT 8: REPORTES

    static void reporteOcupacion() {

        System.out.println("\n--- REPORTE DE OCUPACION ---");
        System.out.println("Capacidad total: " + CAPACIDAD_TOTAL);
        System.out.println("Celdas ocupadas: " + celdasOcupadas);
        System.out.println("Celdas libres: " + celdasLibres);
    }

    static void reporteFinanciero() {

        long totalGeneral = recaudoCarros + recaudoMotos;

        System.out.println("\n--- REPORTE FINANCIERO ---");
        System.out.println("Recaudo por carros: $" + recaudoCarros);
        System.out.println("Recaudo por motos: $" + recaudoMotos);
        System.out.println("Total recaudado: $" + totalGeneral);
    }

}
