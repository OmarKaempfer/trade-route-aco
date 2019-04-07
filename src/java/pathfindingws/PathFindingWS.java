
package pathfindingws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;


@WebService(serviceName = "PathFindingWS")
public class PathFindingWS {

    @WebMethod(operationName = "getBestPath")
    public String getBestPath(@WebParam(name = "jumps") int jumps, 
                        @WebParam(name = "capacity") int capacity) {
        return Launcher.launch(jumps, capacity);
    }
}
