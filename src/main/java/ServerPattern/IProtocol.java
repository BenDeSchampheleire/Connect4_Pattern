package ServerPattern;

import java.io.InputStream;
import java.io.OutputStream;

public interface IProtocol {

    void execute(IContext context, InputStream anInputStream, OutputStream anOutputStream);
}
