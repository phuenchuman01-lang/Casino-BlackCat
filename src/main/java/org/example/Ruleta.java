package org.example;


import java.util.Random;
import java.util.Scanner;
public class Ruleta {
    public static final int MAX_HISTORIAL = 100;
    public static int[] historialNumeros = new int[MAX_HISTORIAL];
    public static int[] historialApuestas = new int[MAX_HISTORIAL];
    public static boolean[] historialAciertos = new boolean[MAX_HISTORIAL];
    public static int historialSize = 0;
    public static Random rng = new Random();
    public static int[] numerosRojos =
            {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        Scanner in = new Scanner(System.in);
        int opcion = 0;

        do {
            mostrarMenu();
            opcion = leerOpcion(in);
            ejecutarOpcion(opcion, in);
        } while (opcion != 3);

        System.out.println("¡Gracias por visitar el Casino Black Cat! Don Donnie te despide.");
        in.close();
    }

    public static void mostrarMenu() {
        System.out.println("\n--- CASINO BLACK CAT ---");
        System.out.println("1. Jugar Ruleta");
        System.out.println("2. Ver Estadísticas");
        System.out.println("3. Salir");
        System.out.print("Selecciona una opción: ");
    }

    public static int leerOpcion(Scanner in) {
        return in.nextInt();
    }

    public static void ejecutarOpcion(int opcion, Scanner in) {
        switch (opcion) {
            case 1:
                System.out.println("\n--- INICIANDO APUESTA ---");
                iniciarRonda(in);
                break;
            case 2:
                System.out.println("\n--- ESTADÍSTICAS ---");
                mostrarEstadisticas();
                break;
            case 3:
                break;
            default:
                System.out.println("Opción no válida. Por favor, elige 1, 2 o 3.");
                break;
        }
    }

    public static void iniciarRonda(Scanner in) {
        char tipoApuesta = leerTipoApuesta(in);

        System.out.print("Ingresa el monto de tu apuesta: $");
        int monto = in.nextInt();

        System.out.println("\n🎲 ¡La ruleta está girando...!");
        int numeroGanador = girarRuleta();
        boolean ganaste = evaluarResultado(numeroGanador, tipoApuesta);
        mostrarResultado(numeroGanador, tipoApuesta, monto, ganaste);
        registrarResultado(numeroGanador, monto, ganaste);
    }

    public static char leerTipoApuesta(Scanner in) {
        System.out.println("¿A qué le vas a apostar?");
        System.out.println("(R) Rojo | (N) Negro | (P) Par | (I) Impar");
        System.out.print("Ingresa la letra de tu apuesta: ");
        return in.next().toUpperCase().charAt(0);
    }

    public static int girarRuleta() {
        return rng.nextInt(37);
    }

    public static boolean evaluarResultado(int numero, char tipo) {
        if (numero == 0) {
            return false;
        }

        switch (tipo) {
            case 'R':
                return esRojo(numero);
            case 'N':
                return !esRojo(numero); // Si NO es rojo (y ya sabemos que no es 0), es negro
            case 'P':
                return numero % 2 == 0; // Es par si el residuo entre 2 es 0
            case 'I':
                return numero % 2 != 0; // Es impar si hay residuo
            default:
                return false;
        }
    }

    public static boolean esRojo(int n) {
        // Recorremos el arreglo de números rojos
        for (int rojo : numerosRojos) {
            if (n == rojo) {
                return true; // Si lo encuentra, es rojo
            }
        }
        return false; // Si termina de buscar y no está, es negro (o cero)
    }

    public static void registrarResultado(int numero, int apuesta, boolean acierto) {
        // Verificamos que no nos pasemos del límite de 100 partidas
        if (historialSize < MAX_HISTORIAL) {
            historialNumeros[historialSize] = numero;
            historialApuestas[historialSize] = apuesta;
            historialAciertos[historialSize] = acierto;

            // Aumentamos el contador para que la próxima jugada se guarde en el siguiente espacio
            historialSize++;
        }
    }

    public static void mostrarResultado(int numero, char tipo, int monto, boolean acierto) {
        System.out.println("¡Ha salido el número " + numero + "!");

        if (acierto) {
            System.out.println("¡Felicidades! Ganaste $" + monto + " con tu apuesta '" + tipo + "'");
        } else {
            System.out.println("Suerte para la próxima... Don Donnie se queda con tus $" + monto);
        }
    }

    public static void mostrarEstadisticas() {
        // Si el contador está en 0, no hay nada que calcular
        if (historialSize == 0) {
            System.out.println("Todavía no has jugado ninguna ronda. ¡Anímate a apostar!");
            return; // Esto hace que la función termine inmediatamente
        }

        int totalApostado = 0;
        int totalAciertos = 0;
        int balanceNeto = 0; 
        for (int i = 0; i < historialSize; i++) {
            int apuestaActual = historialApuestas[i];
            totalApostado += apuestaActual;

            if (historialAciertos[i] == true) {
                totalAciertos++; 
                balanceNeto += apuestaActual; 
            } else {
                balanceNeto -= apuestaActual; 
            }
        }
        double porcentajeAcierto = ((double) totalAciertos / historialSize) * 100;

        System.out.println("Rondas jugadas: " + historialSize);
        System.out.println("Total apostado: $" + totalApostado);
        System.out.println("Total de aciertos: " + totalAciertos);
        System.out.printf("Porcentaje de acierto: %.2f%%\n", porcentajeAcierto);

        if (balanceNeto > 0) {
            System.out.println("Ganancia neta: +$" + balanceNeto + " (¡Le ganaste al casino!)");
        } else if (balanceNeto < 0) {
            System.out.println("Pérdida neta: -$" + Math.abs(balanceNeto) + " (Don Donnie te lo agradece)");
        } else {
            System.out.println("Balance neto: $0 (Quedaste a mano)");
        }
    }
}
