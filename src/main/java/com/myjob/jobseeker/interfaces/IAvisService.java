package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.Avis;
import org.springframework.data.domain.Page;

public interface IAvisService {

   void changeAvis(Avis avis);  
   Page<Avis> getUserAvis(int idCandidate, int page, int size);

}
