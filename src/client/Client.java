package client;

import java.net.*;
import java.io.*;
//Authors
//Piotr Piasta
//Patryk Magnus
public class Client {

    public static void main(String[] args) throws IOException {

        String host = "localhost";
        int port = 0;
        try {
            port = Integer.parseInt("6666");
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy argument: port");
            System.exit(-1);
        }
        //Inicjalizacja gniazda klienckiego
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Nieznany host.");
            System.exit(-1);
        } catch (ConnectException e) {
            System.out.println("Połączenie odrzucone.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Błąd wejścia-wyjścia: " + e);
            System.exit(-1);
        }
        System.out.println("Połączono z " + clientSocket);

        //Deklaracje zmiennych
        String result, username, password, verify, nameP, secondnameP, remittance, withdraw;
        int accnum;
        float sum;

        BufferedReader brSockInp = null;
        BufferedReader brLocalInp = null;
        DataOutputStream out = null;

        //Utworzenie strumieni
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            brSockInp = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            brLocalInp = new BufferedReader(
                    new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Błąd przy tworzeniu strumieni: " + e);
            System.exit(-1);
        }
        //Pętla główna klienta
        //pobieranie loginu i hasla od uzytkownika
        System.out.println("Połączono z Naszym Bankiem!\nProszę się zalogować.\nWprowadź nazwę użytkownika.");
        username = brLocalInp.readLine();
        System.out.println("Wprowadź hasło.");
        password = brLocalInp.readLine();
        //wysylanie loginu i hasla do serwera
        out.writeBytes(username + '\n');
        out.writeBytes(password + '\n');
        out.flush();

        //Sprawdzanie poprawnosci loginu i hasla, odpowiedz od serwera
        verify = brSockInp.readLine();

        if (Integer.parseInt(verify) == 0) {
            System.out.println("Błędny login lub hasło.");
            clientSocket.close();
            return;

        } else System.out.println("Zalogowano pomyślnie!");{
            //wybieranie operacji przez uzytkownika
            System.out.println("Wybierz akcję:\n1. Sprawdź stan konta\n2. Przelew na inne konto\n3. Wypłata środków\n4. Wpłata środków\n5. Wyloguj");

            int choice = (Integer.parseInt(brLocalInp.readLine()));
            out.writeBytes(choice + "\n");

            while (true){
                if (choice == 1) {   //sprawdzanie stanu konta
                    result = brSockInp.readLine();
                    System.out.println("Bieżący stan konta: " + result + "\nDziękujemy za skorzystanie!");
                    break;
                }

                if(choice == 2){    //przelew
                    System.out.println("Podaj imię odbiorcy: ");
                    nameP = brLocalInp.readLine();
                    System.out.println("Podaj nazwisko odbiorcy: ");
                    secondnameP = brLocalInp.readLine();
                    System.out.println("Podaj numer konta odbiorcy: ");
                    accnum = Integer.parseInt(brLocalInp.readLine());
                    System.out.println("Podaj kwotę: ");
                    sum = Float.parseFloat(brLocalInp.readLine());

                    out.writeBytes(nameP + "\n");
                    out.writeBytes(secondnameP + "\n");
                    out.writeBytes(accnum + "\n");
                    out.writeBytes(sum + "\n");

                    verify = brSockInp.readLine();
                    if (Integer.parseInt(verify) == 0) {
                        System.out.println("Brak wystarczających środków!\nSpróbuj ponownie.");
                    }
                    if (Integer.parseInt(verify) == 1) {
                        result = brSockInp.readLine();
                        System.out.println("Wykonano przelew\nNa koncie zostało: " + result + "\nDziękujemy za skorzystanie!");

                    }
                    break;
                }

                if(choice == 3){ //wyplata srodkow z konta
                    System.out.println("Podaj ilość wypłacanych środków");
                    withdraw = brLocalInp.readLine();
                    out.writeBytes(withdraw + "\n");

                    verify = brSockInp.readLine();
                        if (Integer.parseInt(verify) == 0) {
                            System.out.println("Brak wystarczających środków!\nSpróbuj ponownie.");
                        }
                        if (Integer.parseInt(verify) == 1) {
                            result = brSockInp.readLine();
                            System.out.println("Wypłacono środki.\nNa koncie zostało: " + result + "\nDziękujemy za skorzystanie!");

                        }
                        break;
                }

                 if (choice == 4) { //wplata srodkow na konto
                     System.out.println("Podaj ilość wpłacanych środków");
                     remittance = brLocalInp.readLine();
                     if (remittance != null) {
                         out.writeBytes(remittance + "\n");
                         result = brSockInp.readLine();
                         System.out.println("Bieżący stan konta " + result + "\nDziękujemy za skorzystanie!");
                     }
                     break;
                 }

                if (choice == 5) { //wylogowanie
                    clientSocket.close();
                    System.out.println("Wylogowano.\nDziękujemy za skorzystanie!");
                    break;
                }
            }
        }
    }
}








