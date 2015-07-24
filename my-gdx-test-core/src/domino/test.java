package domino;

import com.badlogic.gdx.utils.Array;

public class test {
	/* i want to build an 2d array with this values :
		index[0][0] = 0;
		index[0][1] = 1;
		index[1][0] = 1;
		index[0][2] = 2;
		index[2][0] = 2;
		...
		*/
		
	Array < Array <Integer> > index;
	
	public test( int max){
		index = new Array <Array <Integer> > (false,max);
		for (int aux = 0 ; aux < max ; aux ++){
		Array <Integer> auxArray = new Array<Integer>(false,max);
		for (int aux2 = 0 ; aux2 < max ; aux2++)
			auxArray.insert(aux2,0);

		index.insert(aux, auxArray);
		}
		
		int count = 0;
		for (int pos1 = 0 ; pos1 < max ; pos1++){
			for (int pos2 = pos1 ; pos2 < max ; pos2++){
				index.get(pos1).set(pos2, count); // <-- here fails
				index.get(pos2).set(pos1, count++);
			}
		}
	}
}
