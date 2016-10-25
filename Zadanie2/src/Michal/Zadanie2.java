package Michal;

import java.util.*;

enum TypWierzcholka
{
    WEJSCIE, WYJSCIE, NIE_DOTYCZY
}

/*
Klasa reprezentuje wierzchołek grafu. W klasie tej mamy współrzędne pola labiryntu oraz informację o tym czy
wierzchołek odpowiada zwykłemu polu w labiryncie, czy polu będącemu teleportem. W celu uwzględnienia tego że w zależności
 od tego czy do teleportu wchodzimy z innego pola, czy wychodzimy przeniesieni z wejściowego pola teleportu oznaczamy pola teleportu
  jako wejściowe lub wyjściowe.
 */

class Wierzcholek
{

    private int first;
    private int second;

    public TypWierzcholka getTyp()
    {
        return typ;
    }

    private TypWierzcholka typ; //zakladamy, że typ nigdy nie będzie null

    public Wierzcholek(int first, int second, TypWierzcholka typ)
    {
        this.first = first;
        this.second = second;
        this.typ = typ;
    }

    public int getFirst()
    {
        return first;
    }

    public int getSecond()
    {
        return second;
    }

    public void setFirst(int first)
    {
        this.first = first;
    }

    public void setSecond(int second)
    {
        this.second = second;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wierzcholek wierzcholek = (Wierzcholek) o;

        if (first != wierzcholek.first) return false;
        if (second != wierzcholek.second) return false;
        return typ == wierzcholek.typ;

    }

    @Override
    public int hashCode()
    {
        int result = first;
        result = 31 * result + second;
        result = 31 * result + typ.hashCode();
        return result;
    }
}


public class Zadanie2 {

    private static Scanner input;

    /*Funkcja wczytuje labirynt z konsoli i zapisuje go w postaci dwuwymiarowej tablicy*/
    public static int[][] wczytajLabirynt()
    {
        int szerokosc;
        int wysokosc;
        int[][] L;
        try
        {

            szerokosc = input.nextInt();
            if(szerokosc < 1)
            {
                return null;
            }
            wysokosc = input.nextInt();
            if(wysokosc < 1)
            {
                return null;
            }
            L = new int[wysokosc][szerokosc];
            String[] linia;
            linia = input.nextLine().split("\\s");
            for(int i = 0; i < wysokosc; ++i)
            {
                linia = input.nextLine().split("\\s");
                if(linia.length != szerokosc)
                {
                    return null;
                }
                for(int j = 0; j < szerokosc; ++j)
                {
                    L[i][j] = Integer.parseInt(linia[j]);
                }
            }
        }
        catch(RuntimeException e)
        {
            return null;
        }
        return L;
    }

    /* Funkcja sprawdza, czy w tablicy reprezentującej labirynt znajdują się tylko poprawne liczby. Jeśli tak, to zwraca true,
     * w przeciwnym wypadku false */
    private static boolean czyPoprawneLiczby(int[][] L)
    {
        int wartosc;
        for(int i = 0; i < L.length; ++i)
        {
            for(int j = 0; j < L[i].length; ++j)
            {
                wartosc = L[i][j];
                if(wartosc != 0 && wartosc != 1 && wartosc != 2 && wartosc != 3 && !(wartosc >= 7 && wartosc <= 99))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /* Funkcja zwraca true jeśli w labiryncie znajdują się liczby 2 i 3 - odpowiednio wejście i wyjście z labiryntu, w
     * przeciwnym wypadku zwraca false */
    private static boolean czyWejscieWyjscie(int[][] L)
    {
        int wartosc;
        boolean znalezione = false;
        szukanieWejscia:
        for(int i = 0; i < L.length; ++i)
        {
            for(int j = 0; j < L[i].length; ++j)
            {
                wartosc = L[i][j];
                if(wartosc == 2)
                {
                    znalezione = true;
                    break szukanieWejscia;
                }
            }
        }

        szukanieWyjscia:
        for(int i = 0; i < L.length; ++i)
        {
            for(int j = 0; j < L[i].length; ++j)
            {
                wartosc = L[i][j];
                if(wartosc == 3)
                {
                    znalezione &= true;
                    break szukanieWyjscia;
                }
            }
        }
        return znalezione;
    }

    //sprawdzanie czy w labiryncie teleporty występują parami i jeśli warunek jest spełniony, to jest zwracana wartość true,
    //a w przeciwnym wypadku jest zwracane false.
    // Jeśli nie ma teleportów, to też jest zwracane true
    //zakladamy, że w labiryncie (tablicy L) sa poprawne wartosci liczb.
    private static boolean czySparowaneTeleporty(int[][] L)
    {
        Map<Integer, Integer> teleporty = new HashMap<>();
        int wartosc;
        int ilosc;
        for(int i = 0; i < L.length; ++i)
        {
            for (int j = 0; j < L[i].length; ++j)
            {
                wartosc = L[i][j];
                if (wartosc >= 7)
                {
                    if(teleporty.get(wartosc) == null)
                    {
                        ilosc = 0;
                    }
                    else
                    {
                        ilosc = teleporty.get(wartosc);
                    }
                    teleporty.put(wartosc, ilosc + 1);
                }
            }
        }
        Collection<Integer> wartosci = teleporty.values();
        for(int w: wartosci)
        {
            if(w != 2)
            {
                return false;
            }
        }
        return true;
    }

    /*Funkcja sprawdza, czy wszystkie warunki na poprawność labiryntu są spełnione */
    public static boolean czyPoprawnyLabirynt(int[][] L)
    {
        return L != null && czyPoprawneLiczby(L) && czyWejscieWyjscie(L) && czySparowaneTeleporty(L);
    }

    /*
     znajduje parę liczb całkowitych reprezentujących wyjście z teleportu (współrzędne punktu
      o zadanym numerze różnym niż podane współrzędne) i na tej podstawie tworzy wierzchołek wyjściowy - czyli współrzędne punktu
      o zadanym numerze różnym niż podane współrzędne. Funkcja przyjmuje dwuwymiarową tablicę reprezentującą labirynt L,
     szukany numer, współrzędne punktu będące wejściem do teleportu
     */
    private static Wierzcholek znajdzTeleport(int numer, Wierzcholek polozenie, int[][] L, TypWierzcholka typ)
    {
        Wierzcholek przeciwnyWierzcholek = null;
        for(int i = 0; i < L.length; ++i)
        {
            for(int j = 0; j < L[0].length; ++j)
            {
                if(L[i][j] == numer && (i != polozenie.getFirst() || j != polozenie.getSecond()))
                {
                    przeciwnyWierzcholek = new Wierzcholek(i, j, typ);
                }
            }
        }

        return przeciwnyWierzcholek;

    }

    /*Funkcja zamienia tablicę liczb całkowitych reprezentujących labirynt na graf reprezentujący labirynt w postaci list sąsiedztwa -
      ponieważ wierzchołki grafu są reprezentowane przez pary punktów dlatego do przechowywania grafu została użyta mapa.
      Reprezentacja grafowa gubi informację na temat wejścia i wyjścia z labiryntu. W celu uwzględnienia tego, że pole
      będące teleportem inaczej zachowuje się jak się na niego wchodzi z "normalnego" pola labiryntu, a inaczej zachowuje się jak się
      do niego dostało z innego wejścia teleportu, jest ono reprezentowane jako dwa wierzchołki grafu (jeden posiadający typ WEJSCIE,
      a drugi WYJSCIE). Taka reprezentacja umożliwia zastosowanie niezmienionego algorytmu przeszukiwania grafu w szerz (BFS) do
      znalezienia najkrótszej drogi w labiryncie.
      */
    @SuppressWarnings("unchecked")
    public static Map<Wierzcholek, ArrayList<Wierzcholek> > zbudujGraf(int[][] L)
    {
        Map<Wierzcholek, ArrayList<Wierzcholek>> wynik = new HashMap<>();
        Wierzcholek wierzcholek;
        ArrayList<Wierzcholek> listaSasiedztwa = new ArrayList<>();
        int indeksWysokosc;
        int indeksSzerokosc;
        for(int i = 0; i < L.length; ++i)
        {
            for(int j = 0; j < L[i].length; ++j)
            {
                if(L[i][j] != 1)
                {
                    int wartosc = L[i][j];
                    if(wartosc == 0 || wartosc == 2 || wartosc == 3)
                    {
                        wierzcholek = new Wierzcholek(i, j, TypWierzcholka.NIE_DOTYCZY);
                    }
                    else
                    {
                        wierzcholek = new Wierzcholek(i, j, TypWierzcholka.WYJSCIE);
                    }

                    //wyznaczenie współrzędnych punktów (wierzchołków grafu) połączonych z danym punktem (wierzchołkiem grafu)
                    for (int k = -1; k <= 1; ++k)
                    {
                        for (int l = -1; l <= 1; ++l)
                        {
                            if (k != l && (k == 0 || l == 0))
                            {
                                indeksWysokosc = wierzcholek.getFirst() + k;
                                indeksSzerokosc = wierzcholek.getSecond() + l;
                                if (
                                        indeksWysokosc >= 0 && indeksWysokosc < L.length &&
                                        indeksSzerokosc >= 0 && indeksSzerokosc < L[0].length &&
                                        L[wierzcholek.getFirst() + k][wierzcholek.getSecond() + l] != 1
                                        )
                                {
                                    wartosc = L[indeksWysokosc][indeksSzerokosc];
                                    if(wartosc == 0 || wartosc == 2 || wartosc == 3)
                                    {
                                        listaSasiedztwa.add(new Wierzcholek(indeksWysokosc, indeksSzerokosc, TypWierzcholka.NIE_DOTYCZY));
                                    }
                                    else
                                    {
                                        listaSasiedztwa.add(new Wierzcholek(indeksWysokosc, indeksSzerokosc, TypWierzcholka.WEJSCIE));
                                    }

                                }

                            }
                        }
                    }
                    wynik.put(wierzcholek, (ArrayList<Wierzcholek>)listaSasiedztwa.clone());
                    listaSasiedztwa.clear();

                    wierzcholek = new Wierzcholek(i, j, TypWierzcholka.WEJSCIE);
                    wartosc = L[i][j];
                    listaSasiedztwa.add(znajdzTeleport(wartosc, wierzcholek, L, TypWierzcholka.WYJSCIE));
                    wynik.put(wierzcholek, new ArrayList<>(listaSasiedztwa));
                    listaSasiedztwa.clear();

                }
            }
        }
        return wynik;
    }


    /* Na podstawie mapy list będącej grafową reprezentacją labiryntu oraz tablcy reprezuntującej labirynt
     * wyznaczam kolekcję punktów (par liczb całkowitych) reprezentujących najkrótszą trasę jego przejścia za pomocą
     * algorytmu przeszukiwania grafu w szerz (BFS), którego opis można znalezc na strone http://eduinf.waw.pl/inf/alg/001_search/0127.php
     * W tej funkcj tablica reprezentująca labirynt jest potrzebna do wyznaczenia punktów startu i wyjścia z labiryntu.
    */
    public static ArrayList<Wierzcholek> wyznaczDroge(Map<Wierzcholek, ArrayList<Wierzcholek> > graf, int L[][])
    {
        ArrayList<Wierzcholek> wynik = new ArrayList<>();
        Set<Wierzcholek> czyOdwiedzone = new HashSet<>();
        Map<Wierzcholek, Wierzcholek> poprzednicy = new HashMap<>();

        Wierzcholek poczatek = new Wierzcholek(-1,-1, TypWierzcholka.NIE_DOTYCZY);
        Wierzcholek wyjscie = new Wierzcholek(-1,-1, TypWierzcholka.NIE_DOTYCZY);
        Wierzcholek biezacyWierzcholek;

        // wyznaczenie współrzędnych punktów startu i wyjścia z labiryntu
        for(int i = 0; i < L.length; ++i)
        {
            for(int j = 0; j < L[i].length; ++j)
                if(L[i][j] == 2)
                {
                    poczatek.setFirst(i);
                    poczatek.setSecond(j);
                }
                else if(L[i][j] == 3)
                {
                    wyjscie.setFirst(i);
                    wyjscie.setSecond(j);
                }
        }

        // implementacja algorytmu BFS
        Queue<Wierzcholek> q = new ArrayDeque<>();
        q.add(poczatek);
        poprzednicy.put(poczatek, null);
        czyOdwiedzone.add(poczatek);

        ArrayList<Wierzcholek> zbiorPunktowWierzcholka;
        Iterator<Wierzcholek> it;

        Wierzcholek wierzcholek;
        while(!q.isEmpty())
        {
            biezacyWierzcholek = q.remove();
            if(biezacyWierzcholek.equals(wyjscie))
            {
                break;
            }
            zbiorPunktowWierzcholka = graf.get(biezacyWierzcholek);
            it = zbiorPunktowWierzcholka.iterator();
            while(it.hasNext())
            {
                wierzcholek = it.next();
                if(!czyOdwiedzone.contains(wierzcholek))
                {
                    q.add(wierzcholek);
                    czyOdwiedzone.add(wierzcholek);
                    poprzednicy.put(wierzcholek, biezacyWierzcholek);
                }
            }
        }

        wierzcholek = wyjscie;
        do
        {
            wynik.add(new Wierzcholek(wierzcholek.getSecond(), L.length - wierzcholek.getFirst() - 1, wierzcholek.getTyp()));
            wierzcholek = poprzednicy.get(wierzcholek);
        }
        while(wierzcholek != null);

        return wynik;
    }

    // wypisanie na konsolę par reprezentujących trasę
    public static void wypiszWynik(ArrayList<Wierzcholek> punkty)
    {
        for(int i =  punkty.size() - 1; i >= 0; --i)
        {
            System.out.print( "(" + punkty.get(i).getFirst() + "," + punkty.get(i).getSecond() + ") ");
        }
    }

    public static void main(String[] args)
    {
        input = new Scanner(System.in);
        int iloscLabiryntow = 0;
        try
        {
            iloscLabiryntow = input.nextInt();
        }
        catch(NoSuchElementException e)
        {
            System.out.print("Niepoprawne dane wejsciowe");
        }


        int[][][] labirynty = new int[iloscLabiryntow][][];
        for(int i = 0; i < iloscLabiryntow; ++i)
        {
            labirynty[i] = Zadanie2.wczytajLabirynt();
        }
        input.close();

        BitSet poprawnoscLabiryntow = new BitSet(iloscLabiryntow);

        for(int i = 0; i < iloscLabiryntow; ++i)
        {
            if( Zadanie2.czyPoprawnyLabirynt(labirynty[i]))
            {
                poprawnoscLabiryntow.set(i);
            }
        }

        ArrayList<ArrayList<Wierzcholek>> wyniki = new ArrayList<>();
        for(int i = 0; i < iloscLabiryntow; ++i)
        {
            wyniki.add(null);
        }

        Map<Wierzcholek, ArrayList<Wierzcholek> > graf;
        for(int i = 0; i < iloscLabiryntow; ++i)
        {

            if(poprawnoscLabiryntow.get(i))
            {
                graf = Zadanie2.zbudujGraf(labirynty[i]);
                wyniki.set(i, Zadanie2.wyznaczDroge(graf, labirynty[i]));
            }
        }

        ArrayList<Wierzcholek> wynik = new ArrayList<>();
        for(int i = 0; i < iloscLabiryntow; ++i)
        {
            wynik = wyniki.get(i);
            if(wynik!= null)
            {
                if(wynik.size() == 1)
                {
                    System.out.println("Nie da się przejść przez labirynt");
                }
                else
                {
                    Zadanie2.wypiszWynik(wynik);
                }
                System.out.println();
            }
            else
            {
                System.out.println("Nieprawidłowe dane");
            }

        }
    }

}