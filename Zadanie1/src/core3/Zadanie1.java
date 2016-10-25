package core3;

import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Scanner;


//klasa reprezuntuj¹ca parê liczb ca³kowitych
class IntPair {

	  private int left;
	  private int right;

	  public IntPair(int left, int right) {
	    this.left = left;
	    this.right = right;
	  }

	  public int getLeft() { return left; }
	  public int getRight() { return right; }

	  public void setLeft(int left) { this.left = left; }
	  public void setRight(int right) { this.right = right; }
	  
	  @Override
	  public int hashCode() { return left ^ right; }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof IntPair)) return false;
	    IntPair pairo = (IntPair) o;
	    return this.left == pairo.getLeft() &&
	           this.right == pairo.getRight();
	  }

	}

public class Zadanie1 {

	private static Scanner input;
	
	//wczytujê dane z konsoli do dwuwymiarowej tablicy liczb ca³kowitych
	public static int[][] wczytajLabirynt()
	{
		int szerokosc = 0;
		int wysokosc = 0;
		int[][] L = null;
    	try
    	{
    		szerokosc = input.nextInt();
    		wysokosc = input.nextInt();
    		L = new int[wysokosc][szerokosc]; 
    		for(int i = 0; i < wysokosc; ++i)
    		{
    			for(int j = 0; j < szerokosc; ++j)
    			{
    				L[i][j] = input.nextInt();
    			}
    		}
    	}
    	catch(NoSuchElementException e)
    	{
    		System.out.print("Niepoprawne dane wejsciowe");
    	}
    	return L;			
	}
	
	/* Na podstawie dwuwymiarowej tablicy liczb ca³kowitych reprezentuj¹cej labirynt 
	 * wyznaczam kolekcjê punktów (par liczb ca³kowitych) reprezentuj¹cych najkrótsz¹ trasê jego przejœcia za pomoc¹ 
	 * algorytmu przeszukiwania drzewa w szerz (BFS), którego opis mo¿na znalezc na strone http://eduinf.waw.pl/inf/alg/001_search/0127.php
	*/
	
	public static ArrayList<IntPair> wyznaczDroge(int[][] L)
	{
		IntPair poczatek = new IntPair(-1,-1);
		IntPair wyjscie = new IntPair(-1,-1);
		IntPair biezacyWierzcholek = new IntPair(-1,-1);
	
		// wyznaczenie wspó³rzêdnych punktów startu i wyjœcia z labiryntu
		for(int i = 0; i < L.length; ++i)
		{
			for(int j = 0; j < L[i].length; ++j)
				if(L[i][j] == 2)
			    {
			        poczatek.setLeft(i);
			        poczatek.setRight(j);
			    }
			    else if(L[i][j] == 3)
			    {
			        wyjscie.setLeft(i);
			        wyjscie.setRight(j);
			        L[i][j] = 0;
			    }
		}
		
		// implementacja algorytmu BFS
		Queue<IntPair> q = new ArrayDeque<IntPair>();
		q.add(poczatek);
		int indeksWysokosc = 0;
		int indeksSzerokosc = 0;
		while(!q.isEmpty())
		{
			biezacyWierzcholek = q.remove();
			if(biezacyWierzcholek.equals(wyjscie))
			{
					break;
			}
			
			//wyznaczenie wspó³rzêdnych punktów (wierzcho³ków grafu) po³¹czonych z danym punktem (wierzcho³kiem grafu)
			for (int i = -1;  i <= 1; ++i)
			{
			      for(int j = -1; j <= 1; ++j)
			      {
			    	  if (i != j && (i == 0 || j == 0))
			    	  { 
			    		  indeksWysokosc = biezacyWierzcholek.getLeft() + i;
			    		  indeksSzerokosc = biezacyWierzcholek.getRight() + j;
			    		  
			    		  if(
			    			 indeksWysokosc >= 0 && indeksWysokosc < L.length &&
			    			 indeksSzerokosc >=0 && indeksSzerokosc < L[0].length &&	  
			    			 L[biezacyWierzcholek.getLeft() + i][biezacyWierzcholek.getRight() + j] == 0
			    			)
			    		  {
			    			  // W komórce s¹siada zapisujemy, sk¹d przyszliœmy do niej
			    			  
			    			  if (i == -1)			    				  
			    			  {
			    				  L[indeksWysokosc][indeksSzerokosc] = -4;   // z do³u
			    			  }
			    			  else if (i ==  1)
			    			  {
			    				  L[indeksWysokosc][indeksSzerokosc] = 4;	// z góry
			    			  }
			    			  else if (j == -1)
			    			  {
			    				  L[indeksWysokosc][indeksSzerokosc] = -5;   // z prawej
			    			  }
			    			  else
			    			  {
			    				  L[indeksWysokosc][indeksSzerokosc] = 5;  // z lewej
			    			  }
			    			  
					          q.add(new IntPair(indeksWysokosc, indeksSzerokosc));       // S¹siad zostaje umieszczony w kolejce
			    			  
			    		  }
			    	  }
			      }
			}
		}
		
		ArrayList<IntPair> wynik = new ArrayList<IntPair>();
		
		if(L[wyjscie.getLeft()][wyjscie.getRight()] != 0)
		{
		   int i = wyjscie.getLeft(); 
		   int j = wyjscie.getRight();
		   int c = 0;

				   
		   while((i != poczatek.getLeft()) || (j != poczatek.getRight()))
		   {
			   wynik.add(new IntPair(j, L.length - i - 1));
			   c = L[i][j];
			   switch(c)
			   {
		        	case -4 : i++; break;
		        	case  4 : i--; break;
		        	case -5 : j++; break;
		        	case  5 : j--; break;
			   }
		    }
		    wynik.add(new IntPair(poczatek.getRight(), L.length - poczatek.getLeft() - 1));
		}
		else
		{
			return null;
		}
		
		return wynik;		
	}
	
	// wypisanie na konsolê par reprezentuj¹cych trasê
	public static void wypiszWynik(ArrayList<IntPair> punkty)
	{
		for(int i =  punkty.size() - 1; i >= 0; --i)
		{
			System.out.print( "(" + punkty.get(i).getLeft() + "," + punkty.get(i).getRight() + ") ");
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
    		labirynty[i] = Zadanie1.wczytajLabirynt();
    	}
    	input.close();
    	
    	ArrayList<ArrayList<IntPair>> wyniki = new ArrayList<ArrayList<IntPair>>();
    	for(int i = 0; i < iloscLabiryntow; ++i)
    	{
    		 wyniki.add(Zadanie1.wyznaczDroge(labirynty[i]));
    	}
    	
    	for(int i = 0; i < iloscLabiryntow; ++i)
    	{
    		 Zadanie1.wypiszWynik(wyniki.get(i));
    		 System.out.println();
    	}
	}

}
