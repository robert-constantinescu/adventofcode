package commons;

import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import static commons.WebClientConfig.adventOfCodeClient;

public class GetInput {

    public static Path basePath = Path.of("src/main/resources/input");

    public static Flux<String> getLines(int day ) throws IOException {
        Path filePath = getFilePath(day);
        StringDecoder decoder = StringDecoder.textPlainOnly();
        return DataBufferUtils.readAsynchronousFileChannel(
                        () -> AsynchronousFileChannel.open(filePath, StandardOpenOption.READ),
                        DefaultDataBufferFactory.sharedInstance,
                        16)
                .transform(dataBufferFlux ->
                        decoder.decode(dataBufferFlux, null, null, null));
    }


    public static Flux<String> lines(int day ) throws IOException {
        Path dayPath = getFilePath(day);
        return Flux.using(

                // resource factory creates FileReader instance
                () -> new FileReader("/path/to/file.txt"),

                // transformer function turns the FileReader into a Flux
                reader -> Flux.fromStream(new BufferedReader(reader).lines()),

                // resource cleanup function closes the FileReader when the Flux is complete
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public static Mono<String> getInputFromClient(int day ) {
        // does not really work, need a way to authenticate
        WebClient webClient = adventOfCodeClient();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/2022/day/{day}/input")
                        .build(day))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, exception -> {
                    String responseBodyAs = exception.getResponseBodyAs(String.class);
                    System.out.println(responseBodyAs);
                });
    }

    public static Flux<String> getLocalInput(int day ) throws IOException {
        Path dayPath = getFilePath(day);
        System.out.println(dayPath);
        System.out.println(dayPath.toFile().exists());
        FileOutputStream result_day = new FileOutputStream(basePath.resolve("result_day.txt").toFile());

        return DataBufferUtils
                .readAsynchronousFileChannel(
                        () -> AsynchronousFileChannel.open(dayPath),
                        new DefaultDataBufferFactory(),
                        64)
                .map(DataBuffer::asInputStream)
                .map(db -> {
                    try {
                    result_day.write(db.readAllBytes());
                    result_day.write("\n".getBytes());
                    return db;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(InputStreamReader::new)
                .map(is ->new BufferedReader(is).lines())
                .flatMap(Flux::fromStream);
    }

    public static Mono<byte[]> getLocalInputByte(int day ) throws IOException {
        Path dayPath = getFilePath(day);
        System.out.println(dayPath);
        System.out.println(dayPath.toFile().exists());
        return DataBufferUtils
                .readAsynchronousFileChannel(
                        () -> AsynchronousFileChannel.open(dayPath),
                        new DefaultDataBufferFactory(),
                        64)
                .reduce(DataBuffer::write)
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    System.out.println(new String(bytes));
                    return bytes;
                });

    }

    public static Flux<DataBuffer> getLocalInput2(int day ) throws IOException {
        Path dayPath = getFilePath(day);
        FileInputStream fis = new FileInputStream(dayPath.toFile());
        return DataBufferUtils
                .readInputStream(() -> fis, new DefaultDataBufferFactory(), 2048);
    }

    static Path getFilePath(int day){
        String file = String.format("day_%s.txt", day);
        return basePath.resolve(file);
    }

}
