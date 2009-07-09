package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.web.controller.propertyeditor.MethodEditor;
import cz.strmik.cmmitool.web.controller.propertyeditor.ModelEditor;
import cz.strmik.cmmitool.web.controller.propertyeditor.UserEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * Shared WebBindingInitializer for PetClinic's custom editors.
 *
 * <p>Alternatively, such init-binder code may be put into
 * {@link org.springframework.web.bind.annotation.InitBinder}
 * annotated methods on the controller classes themselves.
 *
 * @author Juergen Hoeller
 */
public class BindingInitializer implements WebBindingInitializer {

	@Autowired
        private GenericDao<Model, String> modelDao;
	@Autowired
        private GenericDao<Method, String> methodDao;
	@Autowired
        private UserDao userDao;

        @Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Model.class, new ModelEditor(this.modelDao));
		binder.registerCustomEditor(Method.class, new MethodEditor(this.methodDao));
		binder.registerCustomEditor(User.class, new UserEditor(this.userDao));
	}

}
