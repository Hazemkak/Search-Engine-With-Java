package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.lang.model.util.Elements;
import java.util.HashMap;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException {
		String[] Random_inks = new String [] {
				"https://en.wikipedia.org/wiki/Main_Page" ,
				"https://en.wikipedia.org/wiki/Fly" ,
				"https://en.wikipedia.org/wiki/Airplane",
				"https://en.wikipedia.org/wiki/Swimming_(sport)",
				"https://en.wikipedia.org/wiki/Apple_Inc.",
				"https://en.wikipedia.org/wiki/Microsoft" ,
				"https://en.wikipedia.org/wiki/Russia",
				"https://en.wikipedia.org/wiki/Sleep",
				"https://en.wikipedia.org/wiki/Coronavirus",
				"https://en.wikipedia.org/wiki/University" ,
				"https://en.wikipedia.org/wiki/Lionel_Messi",
				"https://en.wikipedia.org/wiki/Real_Madrid_CF" ,
				"https://en.wikipedia.org/wiki/Middle_Ages" ,
				"https://en.wikipedia.org/wiki/Mathematics" ,
				"https://en.wikipedia.org/wiki/Signal",
				"https://en.wikipedia.org/wiki/Georgia_(country)"
				,"https://en.wikipedia.org/wiki/Moon",
				"https://en.wikipedia.org/wiki/Space" ,
				"https://en.wikipedia.org/wiki/Universe",
				"https://en.wikipedia.org/wiki/Robot",
				"https://en.wikipedia.org/wiki/Egypt" ,
				"https://en.wikipedia.org/wiki/England",
				"https://en.wikipedia.org/wiki/Computer_science"
				,"https://en.wikipedia.org/wiki/Internet_of_things",
				"https://en.wikipedia.org/wiki/English_language",
				"https://en.wikipedia.org/wiki/Football",
				"https://en.wikipedia.org/wiki/Music",
				"https://en.wikipedia.org/wiki/Piracy",
				"https://en.wikipedia.org/wiki/Mars" ,
				"https://www.forbes.com/?sh=4ceccea2254c"
				,"https://edition.cnn.com/" ,
				"https://en.wikipedia.org/wiki/United_States",
				"https://www.bbc.com/news",
				"https://www.history.com/topics/middle-east/palestine",
				"https://www.mercedes-benz.com/en/",
				"https://www.bmw.com/en/index.html",
				"https://www.audi-mediacenter.com/en",
				"https://www.airarabia.com/ar",
				"https://www.microsoft.com/ar-eg",
				"https://www.amazon.com/",
				"https://www.alibaba.com/",
				"https://en.wikipedia.org/wiki/Rocket",
				"https://www.facebook.com/Officialahlysc/",
				"https://www.liverpoolfc.com/",
				"https://cu.edu.eg/Home",
				"https://www.mit.edu/",
				"https://www.lexico.com/",
				"https://www.merriam-webster.com/",
				"https://en.wikipedia.org/wiki/Algorithm",
				"https://www.duden.de/",
				"https://en.wikipedia.org/wiki/Science"
		};


		int interrupt_detector=1;
		for (int i = 0 ; i < 50; i=i+10 ) {
			Thread s1 = new Thread(new CrawlerHelper(Random_inks[i],interrupt_detector));
			Thread s2 = new Thread(new CrawlerHelper(Random_inks[i + 1],interrupt_detector));
			Thread s3 = new Thread(new CrawlerHelper(Random_inks[i + 2],interrupt_detector));
			Thread s4 = new Thread(new CrawlerHelper(Random_inks[i + 3],interrupt_detector));
			Thread s5 = new Thread(new CrawlerHelper(Random_inks[i + 4],interrupt_detector));


			Thread s6 = new Thread(new CrawlerHelper(Random_inks[i + 5],interrupt_detector));
			Thread s7 = new Thread(new CrawlerHelper(Random_inks[i + 6],interrupt_detector));
			Thread s8 = new Thread(new CrawlerHelper(Random_inks[i + 7],interrupt_detector));
			Thread s9 = new Thread(new CrawlerHelper(Random_inks[i + 8],interrupt_detector));
			Thread s10 = new Thread(new CrawlerHelper(Random_inks[i + 9],interrupt_detector));

			s1.setName("1");
			s2.setName("2");
			s3.setName("3");
			s4.setName("4");
			s5.setName("5");

			s6.setName("6");
			s7.setName("7");
			s8.setName("8");
			s9.setName("9");
			s10.setName("10");


			s1.start();
			s2.start();
			s3.start();
			s4.start();
			s5.start();
			s6.start();
			s7.start();
			s8.start();
			s9.start();
			s10.start();

			interrupt_detector++;
			s1.join();
			s2.join();
			s3.join();
			s4.join();
			s5.join();
			s6.join();
			s7.join();
			s8.join();
			s9.join();
			s10.join();

			System.out.println("Total Memory (in bytes): " + Runtime.getRuntime().totalMemory());
			System.out.println("Free Memory (in bytes): " + Runtime.getRuntime().freeMemory());
			System.out.println("Max Memory (in bytes): " + Runtime.getRuntime().maxMemory());
		}

		SpringApplication.run(DemoApplication.class, args);

	}

}
