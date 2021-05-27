package hr.java.covidportal.niti;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import main.java.sample.Main;
import main.java.sample.PretragaZupanijaController;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class NajviseZarazenihNit implements Runnable{

    private String naziv;


    @Override
    public void run() {

        while(true){

            naziv = PretragaZupanijaController.zupanije.stream().
                    max(Comparator.comparing(z->z.getPostotak())).get().getNaziv();

            System.out.println("Zupanija s najvise zarazenih je " + naziv);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,e ->{

                        naziv = PretragaZupanijaController.zupanije.stream().
                                max(Comparator.comparing(z->z.getPostotak())).get().getNaziv();
                        Main.getMainStage().setTitle("Zupanija s najvise zarazenih je " + naziv);

                    }),new KeyFrame(Duration.seconds(1)));

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            //timeline.play();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
