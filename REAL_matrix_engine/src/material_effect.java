
public class material_effect extends material { // a class that modifies the values from a material.
	
	public int mt_mod=0;
	public int bt_mod=0;
	public int el_mod=0;
	public int pl_mod=0;
	public int dn_mod=0;
	public float pr_mod=0;

	public material_effect() {
	}

	public material_effect(String file, int index) throws Exception {
		super(file, index);
	}

	public material_effect(material new_material) {
		super(new_material);
	}

	public material_effect(int mt, int bt, int el, int pl, int dn, int pr) {
		super(mt, bt, el, pl, dn, pr);
	}
	
	public void change_melting_temp(int new_temp) {
		mt_mod=new_temp;
	}
	public void change_boiling_temp(int new_temp) {
		bt_mod=new_temp;
	}
	public int get_melting_temp() {
		return super.get_melting_temp()+mt_mod;
	}
	public int get_boiling_temp() {
		return super.get_boiling_temp()+bt_mod;
	}
	public int get_plastic() {
		return super.get_plastic()+pl_mod;
	}
	public void change_plastic(int new_plastic) {
		this.pl_mod= new_plastic;
	}
	public int get_elastic() {
		return super.get_elastic()+el_mod;
	}
	public void change_elastic(int new_elastic) {
		this.el_mod= new_elastic;
	}
	public int get_density() {
		return super.get_density()+dn_mod;
	}
	public void change_density(int new_density) {
		this.dn_mod= new_density;
	}
	public float get_pourosity() {
		return super.get_pourosity()+pr_mod;
	}
	public void change_pourosity(int new_pourosity) {
		this.pr_mod = new_pourosity;
	}
	
	
	
	//directly gets each modifer for the material effect
	
	public int get_melting_temp_mod() {
		
		return mt_mod;
	}
	public int get_boiling_temp_mod() {
		return bt_mod;
	}
	public int get_plastic_mod() {
		return pl_mod;
	}
	public int get_elastic_mod() {
		return el_mod;
	}
	public int get_density_mod() {
		return dn_mod;
	}
	public float get_pourosity_mod() {
		return pr_mod;
	}
	
	public void random() {
		mt_mod=(int)(Math.random()*2000);
		bt_mod=mt_mod+(int)(Math.random()*2000);
		el_mod=(int)(Math.random()*200);
		pl_mod=(int)(Math.random()*200);
		dn_mod=(int)(Math.random()*200);
		pr_mod=(float)(Math.random());
		
		
		
	}

}
