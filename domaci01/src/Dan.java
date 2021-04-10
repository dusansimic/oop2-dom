
public enum Dan {
	MON("Ponedeljak"),
	TUE("Utorak"),
	WEN("Sreda"),
	THU("Cetvrtak"),
	FRI("Petak"),
	SAT("Subota"),
	SUN("Nedelja");

	private String n;

	Dan(String n) {
		this.n = n;
	}

	@Override
	public String toString() {
		return n;
	}
}
