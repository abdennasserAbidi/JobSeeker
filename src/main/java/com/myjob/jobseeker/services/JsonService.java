package com.myjob.jobseeker.services;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.myjob.jobseeker.model.Companies;
import com.myjob.jobseeker.model.CompanyModel;
import com.myjob.jobseeker.model.activity.Activities;
import com.myjob.jobseeker.model.activity.ActivityModel;
import com.myjob.jobseeker.model.datalist.Company;
import com.myjob.jobseeker.model.datalist.Field;
import com.myjob.jobseeker.model.datalist.School;
import com.myjob.jobseeker.model.datalist.Subject;
import com.myjob.jobseeker.model.field.FieldModel;
import com.myjob.jobseeker.model.field.Fields;
import com.myjob.jobseeker.model.institute.InstituteModel;
import com.myjob.jobseeker.model.institute.Institutes;
import com.myjob.jobseeker.repo.ActivityRepository;
import com.myjob.jobseeker.repo.CompanyRepository;
import com.myjob.jobseeker.repo.FieldsRepository;
import com.myjob.jobseeker.repo.InstituteRepository;

@Service
public class JsonService {

    private final CompanyRepository companyRepository;
    private final InstituteRepository instituteRepository;
    private final ActivityRepository activityRepository;
    private final FieldsRepository fieldsRepository;
    private static final AtomicInteger idCounter = new AtomicInteger();
    private static final AtomicInteger idCounterSchool = new AtomicInteger();
    private static final AtomicInteger idCounterActivity = new AtomicInteger();
    private static final AtomicInteger idCounterField = new AtomicInteger();

    public JsonService(CompanyRepository companyRepository, 
        InstituteRepository instituteRepository,
        ActivityRepository activityRepository, 
        FieldsRepository fieldsRepository
    ) {
        this.companyRepository = companyRepository;
        this.instituteRepository = instituteRepository;
        this.activityRepository = activityRepository;
        this.fieldsRepository = fieldsRepository;
    }

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY NAMES
    ///////////////////////////////////////////////////////////////////////////
    public long getCompaniesCount() {
      return companyRepository.count();
    }

    public String saveCompany(CompanyModel company) {
        boolean isThere = false;
        List<CompanyModel> list = companyRepository.findAll();
        List<CompanyModel> reponnse;

        for(CompanyModel companyModel : list) {
          if (companyModel.getName().equals(company.getName())) {
            isThere = true;
          }
        }

        if (!isThere) {
          companyRepository.delete(list.get(list.size() - 1));
          List<CompanyModel> withoutOther = companyRepository.findAll();
          int id = withoutOther.get(withoutOther.size() - 1).getId();
      
          company.setId(idCounter.accumulateAndGet(id, (current, x) -> current + 1));
          withoutOther.add(company);
          CompanyModel other = new CompanyModel();
          other.setId(idCounter.incrementAndGet());
          other.setName("Autres");
          withoutOther.add(other);
          reponnse = companyRepository.saveAll(withoutOther);

          if (reponnse != null) return "Ajout avec success";
          else return "Ajout a échoué"; 
        }
        return "Non éxistant"; 
    }

    public void saveFromJsonFile(Companies companies) {
        try {
          for(Company company : companies.getCompanies()) {

            CompanyModel companyModel = new CompanyModel();
            companyModel.setId(idCounter.incrementAndGet());
            companyModel.setName(company.getLibelly());  
				    companyRepository.save(companyModel);
			   }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public List<CompanyModel> getListCompanies() {
      return companyRepository.findAll();
    }

    public Page<CompanyModel> getListCompanies(int page, int size) {

      List<CompanyModel> companies = companyRepository.findAll();

      PageRequest pageable = PageRequest.of(page - 1, size);
      final int start = (int) pageable.getOffset();
      final int end = Math.min((start + pageable.getPageSize()), companies.size());

      Page<CompanyModel> pager;

      if (start < companies.size() && start < end) {
          pager = new PageImpl<>(companies.subList(start, end), pageable, companies.size());
      } else pager = new PageImpl<>(Collections.emptyList(), pageable, companies.size());

      return pager;
    }


    ///////////////////////////////////////////////////////////////////////////
    // INSTITUTE NAMES
    ///////////////////////////////////////////////////////////////////////////
    public long getSchoolsCount() {
      return instituteRepository.count();
    }

    public String saveInstitute(InstituteModel instituteModel) {
        boolean isThere = false;
        List<InstituteModel> list = instituteRepository.findAll();
        List<InstituteModel> reponse;

        for(InstituteModel instituteModelss : list) {
          if (instituteModelss.getName().equals(instituteModel.getName())) {
            isThere = true;
          }
        }

        if (!isThere) {
          instituteRepository.delete(list.get(list.size() - 1));
          List<InstituteModel> withoutOther = instituteRepository.findAll();
          int id = withoutOther.get(withoutOther.size() - 1).getId();
          System.out.println("kyltyktyyjjtyty    "+id);
          instituteModel.setId(idCounterSchool.accumulateAndGet(id, (current, x) -> current + 1));
          withoutOther.add(instituteModel);
          InstituteModel other = new InstituteModel();
          other.setId(idCounterSchool.incrementAndGet());
          other.setName("Autres");
          withoutOther.add(other);
          reponse = instituteRepository.saveAll(withoutOther);

          if (reponse != null) return "Ajout avec success";
          else return "Ajout a échoué"; 
        }
        return "Non éxistant";
    }
   
    public void saveInstitutesFromJsonFile(Institutes institutes) {
        try {
          for(School school : institutes.getSchool()) {

            InstituteModel instituteModel = new InstituteModel();
            instituteModel.setId(idCounterSchool.incrementAndGet());
            instituteModel.setName(school.getLibelly());  
				    instituteRepository.save(instituteModel);
			   }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public List<InstituteModel> getAllInstitutes() {
      return instituteRepository.findAll();
    }

    public Page<InstituteModel> getAllInstitues(int page, int size) {

      List<InstituteModel> schools = instituteRepository.findAll();

      PageRequest pageable = PageRequest.of(page - 1, size);
      final int start = (int) pageable.getOffset();
      final int end = Math.min((start + pageable.getPageSize()), schools.size());

      Page<InstituteModel> pager;

      if (start < schools.size() && start < end) {
          pager = new PageImpl<>(schools.subList(start, end), pageable, schools.size());
      } else pager = new PageImpl<>(Collections.emptyList(), pageable, schools.size());

      return pager;
    }


    ///////////////////////////////////////////////////////////////////////////
    // ACTIVITY NAMES
    ///////////////////////////////////////////////////////////////////////////
    public long getActivityCount() {
      return activityRepository.count();
    }

    public String saveActivity(ActivityModel activityModel) {
        boolean isThere = false;
        List<ActivityModel> list = activityRepository.findAll();
        List<ActivityModel> reponse;

        for(ActivityModel activityModelss : list) {
          if (activityModelss.getName().equals(activityModel.getName())) {
            isThere = true;
          }
        }

        if (!isThere) {
          activityRepository.delete(list.get(list.size() - 1));
          List<ActivityModel> withoutOther = activityRepository.findAll();
          int id = withoutOther.get(withoutOther.size() - 1).getId();
          activityModel.setId(idCounterActivity.accumulateAndGet(id, (current, x) -> current + 1));
          withoutOther.add(activityModel);
          ActivityModel other = new ActivityModel();
          other.setId(idCounterActivity.incrementAndGet());
          other.setName("Autres");
          withoutOther.add(other);
          reponse = activityRepository.saveAll(withoutOther);

          if (reponse != null) return "Ajout avec success";
          else return "Ajout a échoué"; 
        }
        return "Non éxistant";
    }
   
    public void saveActivitiesFromJsonFile(Activities activities) {
        try {
          for(Subject activity : activities.getSubject()) {

            ActivityModel activityModel = new ActivityModel();
            activityModel.setId(idCounterActivity.incrementAndGet());
            activityModel.setName(activity.getLibelly());  
				    activityRepository.save(activityModel);
			   }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public List<ActivityModel> getAllActivities() {
      return activityRepository.findAll();
    }

    public Page<ActivityModel> getAllActivities(int page, int size) {

      List<ActivityModel> schools = activityRepository.findAll();

      PageRequest pageable = PageRequest.of(page - 1, size);
      final int start = (int) pageable.getOffset();
      final int end = Math.min((start + pageable.getPageSize()), schools.size());

      Page<ActivityModel> pager;

      if (start < schools.size() && start < end) {
          pager = new PageImpl<>(schools.subList(start, end), pageable, schools.size());
      } else pager = new PageImpl<>(Collections.emptyList(), pageable, schools.size());

      return pager;
    }


    ///////////////////////////////////////////////////////////////////////////
    // FIELD NAMES
    ///////////////////////////////////////////////////////////////////////////
    public long getFieldCount() {
      return fieldsRepository.count();
    }

    public String saveField(FieldModel fieldModel) {
        boolean isThere = false;
        List<FieldModel> list = fieldsRepository.findAll();
        List<FieldModel> reponse;

        for(FieldModel activityModelss : list) {
          if (activityModelss.getName().equals(fieldModel.getName())) {
            isThere = true;
          }
        }

        if (!isThere) {
          fieldsRepository.delete(list.get(list.size() - 1));
          List<FieldModel> withoutOther = fieldsRepository.findAll();
          int id = withoutOther.get(withoutOther.size() - 1).getId();
          fieldModel.setId(idCounterField.accumulateAndGet(id, (current, x) -> current + 1));
          withoutOther.add(fieldModel);
          FieldModel other = new FieldModel();
          other.setId(idCounterField.incrementAndGet());
          other.setName("Autres");
          withoutOther.add(other);
          reponse = fieldsRepository.saveAll(withoutOther);

          if (reponse != null) return "Ajout avec success";
          else return "Ajout a échoué"; 
        }
        return "Non éxistant";
    }
   
    public void saveFieldsFromJsonFile(Fields fields) {
        try {
          for(Field activity : fields.getStudyfields()) {

            FieldModel activityModel = new FieldModel();
            activityModel.setId(idCounterField.incrementAndGet());
            activityModel.setName(activity.getLibelly());  
				    fieldsRepository.save(activityModel);
			   }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public List<FieldModel> getAllFields() {
      return fieldsRepository.findAll();
    }

    public Page<FieldModel> getAllFields(int page, int size) {

      List<FieldModel> schools = fieldsRepository.findAll();

      PageRequest pageable = PageRequest.of(page - 1, size);
      final int start = (int) pageable.getOffset();
      final int end = Math.min((start + pageable.getPageSize()), schools.size());

      Page<FieldModel> pager;

      if (start < schools.size() && start < end) {
          pager = new PageImpl<>(schools.subList(start, end), pageable, schools.size());
      } else pager = new PageImpl<>(Collections.emptyList(), pageable, schools.size());

      return pager;
    }
}
