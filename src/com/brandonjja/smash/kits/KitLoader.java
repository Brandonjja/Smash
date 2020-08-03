package com.brandonjja.smash.kits;

import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jiggly;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

public class KitLoader {
	public static void init() {
		Blink.load();
		Metoo.load();
		Pika.load();
		Toshi.load();
		Shadow.load();
		Jiggly.load();
	}
}
