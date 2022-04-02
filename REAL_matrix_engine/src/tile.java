import java.util.Arrays;

public class tile {
	// Each plane is made out of a matrix of tiles. 
	// each tile has a stack of items. 
	
	public boolean visible=true;
	private item[] stack= {};
	private boolean sorted=false;
	private char priority_graphic=0;
	//private item[] big_items;  // array containing references to large but pushable objects. 
	private int max_push;
	//
//	public item[] get_big_items() {
//		return big_items;
//	}
//
//
//	public void set_big_items(item[] big_items) {
//		this.big_items = big_items;
//		
//		
//	}

	
	public item get_hot_item() {
		return stack[stack.length-1];
	}
	
	public int get_max_push() {
		return max_push;
	}


	public void set_max_push(int max_push) {
		this.max_push = max_push;
	}


	//private effect[] area_effects= {null}; //not yet implemented. AoE effects should  modify items in the stack. 
	public tile(){
		
	}
	

	public tile(boolean new_visible){
		visible=new_visible;
		
		
	}
	
	public void clear_stack() {
		stack= new item[0];
	}
	
	
	
	public void temp_sort() { // sorts all the items by their temperature, so I can have an easier time of distributing the temperatures. 
		
		quickSort(stack, 0, stack.length-1); 
		sorted=true;

	}
	
	
	public void temp_exchange(item[] temp_stack) { // looks at the hottest item, and tries to distribute it's temperature to the other items in the stack. 
		
		
		if (temp_stack.length>1) {
			
			if(! sorted) {
				temp_sort();
			}
			
			float temp_exchange=(float)matrix_engine.material_list.get_material(temp_stack[temp_stack.length-1].get_material()).get_tconduct(); // the speed of temperature transmission
			int temp_dif=(temp_stack[temp_stack.length-1].get_temp()-temp_stack[0].get_temp());
			int temp_spread=temp_dif/(temp_stack.length); // the difference in temperature, divided by the number of objects
			for(int i=0; i<temp_stack.length-1;i++) {
				
				temp_stack[i].set_temp((int)(temp_stack[i].get_temp()+temp_spread*temp_exchange)); // add the temperature to each item
				temp_stack[i].temp_change();
			}
			temp_stack[temp_stack.length-1].set_temp((int)(temp_stack[temp_stack.length-1].get_temp()-(temp_spread*(temp_stack.length-1))*temp_exchange));
			temp_stack[temp_stack.length-1].temp_change();
			
			if(temp_stack[temp_stack.length-1].get_temp()<temp_stack[temp_stack.length-2].get_temp()) {
				sorted=false;
			}
			
		}else if(temp_stack.length==1) {
			temp_stack[0].temp_change();
		}
	}
	
	
	
	
	
	private void quickSort(item arr[], int begin, int end) { // copied from: https://www.baeldung.com/java-quicksort, modified to work with temp
	    if (begin < end) {
	        int partitionIndex = partition(arr, begin, end);

	        quickSort(arr, begin, partitionIndex-1);
	        quickSort(arr, partitionIndex+1, end);
	    }
	}
	
	private int partition(item arr[], int begin, int end) { // copied from: https://www.baeldung.com/java-quicksort, modified to work with temp
	    int pivot = arr[end].get_temp();
	    int i = (begin-1);

	    for (int j = begin; j < end; j++) {
	        if (arr[j].get_temp() <= pivot) {
	            i++;

	            item swapTemp = arr[i];
	            arr[i] = arr[j];
	            arr[j] = swapTemp;
	        }
	    }

	    item swapTemp = arr[i+1];
	    arr[i+1] = arr[end];
	    arr[end] = swapTemp;

	    return i+1;
	}
	
	public void add_item(item new_item) {
		stack = Arrays.copyOf(stack, stack.length+1);
		stack[stack.length-1]=new_item;
		
		if(new_item.get_form().get_basic_form().get_volume()>1000 && new_item.get_push_size()>1000) { 
//			if(big_items==null) {
//				big_items=new item[1];
//				
//			}else {
//				big_items = Arrays.copyOf(big_items, big_items.length+1);
//			}
//			big_items[big_items.length-1]=new_item;
			max_push=Math.max(new_item.get_push_size(), max_push);
			
			//quickSort(big_items, 0,big_items.length-1);
			
			if(new_item.is_priority()) {set_Priority_graphic(new_item.get_graphic());}
		}else {
			stack = Arrays.copyOf(stack, stack.length+1);
			stack[stack.length-1]=new_item;
			
			quickSort(stack, 0, stack.length-1);
			
			if(new_item.is_priority()) {set_Priority_graphic(new_item.get_graphic());}
		}
		
		
		
		
	}
	
	public void add_item(item[] new_item) {
		for(int i=0; i<new_item.length;i++) {
			stack = Arrays.copyOf(stack, stack.length+1);
			stack[stack.length-1]=new_item[i];
			
//			if(new_item[i]!= null && new_item[i].is_big()) { 
//				if(big_items==null) {
//					big_items=new item[1];
//				}else {
//					big_items = Arrays.copyOf(big_items, big_items.length+1);
//				}
//				big_items[big_items.length-1]=new_item[i];
//			}
			
			quickSort(stack, 0, stack.length-1);
			//sorted=false;
			if(new_item[i].is_priority()) {set_Priority_graphic(new_item[i].get_graphic());}
			
		}
		
	}
	
	
	
	public item[] get_stack() {
		return stack;
	}
	
	public void set_stack(item[] new_stack) {
		this.stack=new_stack;
	}
	
	public item pop(int index) {
		item  output= stack[index];
		
		//item[] second_half_of_stack=Arrays.copyOfRange(stack,index, stack.length);
		
		System.arraycopy(stack, index+1, stack, index, stack.length-index-1);
		
		return output;
		
	}
	
	
	public void random(int num_of_items) { // makes a tile with completely random items
		stack = new item[num_of_items];
		for(int i=0; i<num_of_items; i++) {
			stack[i]=new item();
			stack[i].random();
			sorted=false;
		}
		//visible=true;
	}
	
	public tile copy() {  // method to duplicate a tile 
		tile new_tile = new tile();
		
		item[] temp_stack=stack.clone();
		for(int i=0; i<stack.length; i++) {
			temp_stack[i]=temp_stack[i].copy();
		}
		new_tile.set_stack(temp_stack);
		new_tile.visible=visible;
		new_tile.sorted=sorted;
		
		return new_tile;
	}


	public char get_Priority_graphic() {
		return priority_graphic;
	}


	public void set_Priority_graphic(char priority_graphic) {
		this.priority_graphic = priority_graphic;
	}


//	public boolean is_big() {
//		return big;
//	}
//
//
//	public void set_big(boolean big) {
//		this.big = big;
//	}
	
}
