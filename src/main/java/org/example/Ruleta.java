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
        // Abrimos el Scanner aquí para usarlo en todo el menú
        Scanner in = new Scanner(System.in);
        int opcion = 0;

        do {
            mostrarMenu();
            opcion = leerOpcion(in); // Leemos lo que teclea el usuario
            ejecutarOpcion(opcion, in); // Ejecutamos la jugada
        } while (opcion != 3); // El 3 es la opción para salir

        System.out.println("¡Gracias por visitar el Casino Black Cat! Don Donnie te despide.");
        in.close(); // Siempre es buena práctica cerrar el Scanner al terminar
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
                iniciarRonda(in); // Llama a la función del juego
                break;
            case 2:
                System.out.println("\n--- ESTADÍSTICAS ---");
                mostrarEstadisticas(); // Llama a la función de estadísticas
                break;
            case 3:
                // No hacemos nada aquí porque el mensaje de despedida ya está en menu()
                break;
            default:
                // Si el jugador teclea un 4, un 9 o cualquier otro número
                System.out.println("Opción no válida. Por favor, elige 1, 2 o 3.");
                break;
        }
    }

    public static void iniciarRonda(Scanner in) {
        // 1. Usamos la función que creaste para pedir el tipo de apuesta
        char tipoApuesta = leerTipoApuesta(in);

        // 2. Pedimos el dinero (¡es un casino después de todo!)
        System.out.print("Ingresa el monto de tu apuesta: $");
        int monto = in.nextInt();

        // 3. Hacemos girar la ruleta con tu otra función
        System.out.println("\n🎲 ¡La ruleta está girando...!");
        int numeroGanador = girarRuleta();

        // TODO: Falta evaluar si ganamos o perdimos (lo haremos en el siguiente paso)

        // 4. El juez decide
        boolean ganaste = evaluarResultado(numeroGanador, tipoApuesta);

        // 5. Mostramos el resultado
        mostrarResultado(numeroGanador, tipoApuesta, monto, ganaste);

        // 6. Guardamos la jugada en los libros de contabilidad
        registrarResultado(numeroGanador, monto, ganaste);
    }

    public static char leerTipoApuesta(Scanner in) {
        System.out.println("¿A qué le vas a apostar?");
        System.out.println("(R) Rojo | (N) Negro | (P) Par | (I) Impar");
        System.out.print("Ingresa la letra de tu apuesta: ");

        // Leemos la palabra, la pasamos a mayúsculas y nos quedamos con la primera letra
        return in.next().toUpperCase().charAt(0);
    }

    public static int girarRuleta() {
        // Genera un número aleatorio desde 0 hasta 36 (el 37 es el límite superior excluido)
        return rng.nextInt(37);
    }

    public static boolean evaluarResultado(int numero, char tipo) {
        // Regla de oro de la ruleta: Si sale 0, las apuestas sencillas pierden
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
        int balanceNeto = 0; // Ganancia o pérdida total

        // Un ciclo 'for' que revisará la libreta de contabilidad línea por línea
        for (int i = 0; i < historialSize; i++) {
            int apuestaActual = historialApuestas[i];
            totalApostado += apuestaActual; // Sumamos el dinero al pozo total

            if (historialAciertos[i] == true) {
                totalAciertos++; // Sumamos un acierto
                balanceNeto += apuestaActual; // Ganó, suma la apuesta a su bolsillo
            } else {
                balanceNeto -= apuestaActual; // Perdió, resta la apuesta de su bolsillo
            }
        }

        // Calculamos el porcentaje. Ponemos (double) para que no nos borre los decimales.
        double porcentajeAcierto = ((double) totalAciertos / historialSize) * 100;

        // Imprimimos el reporte final
        System.out.println("Rondas jugadas: " + historialSize);
        System.out.println("Total apostado: $" + totalApostado);
        System.out.println("Total de aciertos: " + totalAciertos);
        System.out.printf("Porcentaje de acierto: %.2f%%\n", porcentajeAcierto); // %.2f muestra solo 2 decimales

        if (balanceNeto > 0) {
            System.out.println("Ganancia neta: +$" + balanceNeto + " (¡Le ganaste al casino!)");
        } else if (balanceNeto < 0) {
            System.out.println("Pérdida neta: -$" + Math.abs(balanceNeto) + " (Don Donnie te lo agradece)");
        } else {
            System.out.println("Balance neto: $0 (Quedaste a mano)");
        }
    }
}