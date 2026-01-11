package es.us.dp1.lIng_04_25_26.endofline.developers;

import org.apache.maven.model.Developer;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class DeveloperService {

    private List<Developer> developers;

    public Page<Developer> getDevelopers(Pageable pageable) {
        if (developers == null) {
            loadDevelopers();
        }

        Integer total = developers.size();
        Integer start = (int) pageable.getOffset();
        Integer end = Math.min((start + pageable.getPageSize()), total);

        List<Developer> content = start > total ? List.of() : developers.subList(start, end);

        return new PageImpl<>(content, pageable, total);
    }

    private void loadDevelopers(){
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader("pom.xml"));
            developers = model.getDevelopers();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
