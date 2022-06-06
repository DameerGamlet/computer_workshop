package com.thesis.computer_workshop.controller;

import com.thesis.computer_workshop.Variables;
import com.thesis.computer_workshop.models.application.RepairApplication;
import com.thesis.computer_workshop.models.basket.Basket;
import com.thesis.computer_workshop.models.buy.CompletedApplication;
import com.thesis.computer_workshop.models.images.ImageNoteBook;
import com.thesis.computer_workshop.models.logs.LogProduct;
import com.thesis.computer_workshop.models.mail.MaiLSender;
import com.thesis.computer_workshop.models.products.Notebook;
import com.thesis.computer_workshop.models.users.Usr;
import com.thesis.computer_workshop.repositories.applicationRepositories.RepairApplicationRepository;
import com.thesis.computer_workshop.repositories.applicationRepositories.RepairRepository;
import com.thesis.computer_workshop.repositories.basketRepositories.BasketRepository;
import com.thesis.computer_workshop.repositories.completedApplicationRepositories.CompletedApplicationRepository;
import com.thesis.computer_workshop.repositories.imagesRepositories.ImageNoteBookRepository;
import com.thesis.computer_workshop.repositories.logsRepositories.LogProductRepository;
import com.thesis.computer_workshop.repositories.productsRepositories.NotebookRepository;
import com.thesis.computer_workshop.repositories.usersRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    public NotebookRepository notebookRepository;
    @Autowired
    public ImageNoteBookRepository imageNoteBookRepository;
    @Autowired
    public LogProductRepository logProductRepository;
    @Autowired
    public RepairApplicationRepository repairApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RepairRepository repairRepository;
    @Autowired
    private MaiLSender maiLSender;
    @Autowired
    public BasketRepository basketRepository;
    @Autowired
    public CompletedApplicationRepository completedApplicationRepository;

    // КОНТРОЛЛЕРЫ ДЛЯ ПАНЕЛИ АДМИНИСТРАТОРА
    // открытие панели
    @GetMapping("/admin")
    public String returnAdminCatalog(Model model) {
        return "/admin/catalog_for_edit";
    }

    // ЗАЯВКИ НА РЕМОНТ
    @GetMapping("/admin/application/archive")
    public String returnFinalArchiveApplication(Model model) {
        return "/applications/admin_application_final";
    }

    @GetMapping("/admin/application/archive")
    public String returnArchiveApplication(Model model) {
        return "/applications/admin_application_archive";
    }

    @GetMapping("/admin/application")
    public String returnNewApplicationList(Model model) {
        Iterable<RepairApplication> applicationIterable = repairApplicationRepository.findAll();
        model.addAttribute("applicationIterable", applicationIterable);
        return "/applications/admin_application_list";
    }

    @PostMapping("/admin/application/{id}/delete")
    public String deleteApplication(@PathVariable(value = "id") long id, Model model) throws IOException {
        RepairApplication repairApplication = repairApplicationRepository.findById(id).orElseThrow();
        repairApplicationRepository.delete(repairApplication);
        return "redirect:/admin/application";
    }
    // прочие контроллеры по данному разделу ...

    // Контроллер для работы с товарами "Ноутбук"
    @GetMapping("/admin/notebook")
    public String returnAdminNotebook(Model model) {
        Iterable<Notebook> notebooks = notebookRepository.findAll();
        model.addAttribute("products", notebooks);
        return "/admin/admin_products/notebook/edit_notebook";
    }

    @PostMapping("/admin/notebook")
    public String setAdminNotebook(@RequestParam String name,
                                   Notebook notebook,
                                   Model model,
                                   @RequestParam("file1") MultipartFile file1,
                                   @RequestParam("file2") MultipartFile file2,
                                   @RequestParam("file3") MultipartFile file3,
                                   @RequestParam("file4") MultipartFile file4,
                                   @RequestParam("file5") MultipartFile file5,
                                   @RequestParam("file6") MultipartFile file6) throws IOException {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        notebook.setDateTimeCreate(Timestamp.valueOf(currentTime));

        ImageNoteBook image1 = addImage(notebook, file1),
                image2 = addImage(notebook, file2),
                image3 = addImage(notebook, file3),
                image4 = addImage(notebook, file4),
                image5 = addImage(notebook, file5),
                image6 = addImage(notebook, file6);
        image1.setPreviewImage(true);

        Notebook notebookFromDB = notebookRepository.save(notebook);
        notebookFromDB.setPreviewImageId(notebookFromDB.getImageNoteBooksList().get(0).getId());
        notebookRepository.save(notebook);

        System.out.println("Добавлен товар \"" + notebook.getName()
                + "\" ценой " + notebook.getPrice() + " (" + notebook.getDateTimeCreate() + ")");

        LogProduct newProduct = new LogProduct();
        newProduct.setCategory("Ноутбук");
        newProduct.setAction("Добавление товара");
        newProduct.setIdProduct(notebook.getId());
        newProduct.setNameProduct(notebook.getName());
        newProduct.setDescription("Добавлен товар \"" + notebook.getName() + "\" ценой " + notebook.getPrice() + " (" + notebook.getDateTimeCreate() + ")");
        logProductRepository.save(newProduct);

        return "/admin/admin_products/notebook/edit_notebook";
    }

    @GetMapping("/admin/notebook/{id}/edit")
    public String returnEditNotebook(@PathVariable(value = "id") long id, Model model) {
        if (!notebookRepository.existsById(id)) {
            return "/admin/admin_products/notebook/update_notebook";
        }
        Optional<Notebook> notebooks = notebookRepository.findById(id);
        ArrayList<Notebook> result = new ArrayList<>();
        notebooks.ifPresent(result::add);
        model.addAttribute("notebook", result);
        return "/admin/admin_products/notebook/update_notebook";
    }

    @PostMapping("/admin/notebook/{id}/edit")
    public String setAdminNotebook(@PathVariable(value = "id") long id,
                                   @RequestParam String name, Notebook notebook, Model model,
                                   @RequestParam("file1") MultipartFile file1,
                                   @RequestParam("file2") MultipartFile file2,
                                   @RequestParam("file3") MultipartFile file3,
                                   @RequestParam("file4") MultipartFile file4,
                                   @RequestParam("file5") MultipartFile file5,
                                   @RequestParam("file6") MultipartFile file6) throws IOException {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        notebook.setDateTimeCreate(Timestamp.valueOf(currentTime));

        ImageNoteBook image1 = addImage(notebook, file1),
                image2 = addImage(notebook, file2),
                image3 = addImage(notebook, file3),
                image4 = addImage(notebook, file4),
                image5 = addImage(notebook, file5),
                image6 = addImage(notebook, file6);
        image1.setPreviewImage(true);

        Notebook notebookFromDB = notebookRepository.findById(id).orElseThrow();
        notebookRepository.save(notebook);
        notebookFromDB.setPreviewImageId(notebookFromDB.getImageNoteBooksList().get(0).getId());
        notebookRepository.save(notebook);

        return "redirect:/admin/notebook";
    }

    @PostMapping("/admin/notebook/{id}/delete")
    public String deleteNotebookById(@PathVariable(value = "id") long id, Model model) throws IOException {
        Notebook notebook = notebookRepository.findById(id).orElseThrow();
        notebookRepository.delete(notebook);
        return "redirect:/admin/notebook";
    }

    private ImageNoteBook toImageEntity(MultipartFile file) throws IOException {
        ImageNoteBook image = new ImageNoteBook();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    private ImageNoteBook addImage(Notebook notebook, MultipartFile file) throws IOException {
        ImageNoteBook image = new ImageNoteBook();
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            notebook.addImageToNotebook(image);
        }
        return image;
    }
    // прочие контроллеры по данному разделу ...

    // КОНТРОЛЕР для работы с заявками
    @GetMapping("/application")
    public String applicationPage(Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        model.addAttribute("user_name", user.getName());
        model.addAttribute("user_email", user.getEmail());
        model.addAttribute("user_number", user.getPhoneNumber());
        return "applications/application_form";
    }

    @PostMapping("/application")
    public String setApplication(Principal principal,
                                 Model model,
                                 @RequestParam("file1") MultipartFile file1,
                                 @RequestParam("file2") MultipartFile file2,
                                 @RequestParam("file3") MultipartFile file3,
                                 @RequestParam(name = "category") String category,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "number") String number,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "data") String data) throws IOException {
        RepairApplication repairApplication = new RepairApplication();
        ImagesApplication image1 = addImage(repairApplication, file1),
                image2 = addImage(repairApplication, file2),
                image3 = addImage(repairApplication, file3);
        Usr user = getUserByPrincipal(principal);

        String character = "Работает, но с зависаниями. Самопроизвольно выключается. Сильно нагревается. Сильно гудит вентилятор.";
        String location = "В мастерской магазина услуг.";
        repairApplication.setUsername(user.getUsername());
        repairApplication.setName(user.getName());
        repairApplication.setEmail(user.getEmail());
        repairApplication.setAddress(address);
        repairApplication.setCategory(category);
        repairApplication.setDescription(description);
        repairApplication.setCharacter(character);
        repairApplication.setLocation(location);
        repairApplication.setData(data);
        repairRepository.save(repairApplication);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String dateMessage = repairApplication.getDateOfCreated().toString().split("T")[0];
            String timeMessage = repairApplication.getDateOfCreated().toString().split("T")[1].split("\\.")[0];
            String messageClient = "Добрый день, " + user.getName()
                    + "!\nСпасибо за вашу заявку на нашем сервисе\n"
                    + "\nВы оставили следующую заявку:\n" + "-".repeat(50)
                    + "\nСломался: \"" + category + "\""
                    + "\nХарактер ошибки следующий:\n"
                    + String.join("\n\t", character.split("\\."))
                    + "\nДата оформления: " + dateMessage +
                    " и время оформления " + timeMessage;
            maiLSender.send(user.getEmail(), "Заявка на услуга ремонта от " + dateMessage + " " + timeMessage, messageClient);

            String messageShop = "Заявка от " + user.getName() + "\nEmail: " + user.getEmail()
                    + "\nНомер телефона: " + number
                    + "\nСломался " + category
                    + "\nОписание: " + description
                    + "\nХарактер ошибки следующий:\n\t"
                    + String.join("\n\t", character.split("\\."));
            maiLSender.send("sar.comp.workshop@gmail.com", "Заявка на услуга ремонта от " + dateMessage + " " + timeMessage, messageShop);
        }
        return "applications/application_form";
    }

    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }

    // КОНТРОЛЛЕР для работы с корзиной
    @GetMapping("/basket")
    public String returnBasketList(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        Iterable<Basket> baskets = basketRepository.findAll();
        int counts = (int) baskets.spliterator().getExactSizeIfKnown();
        model.addAttribute("baskets", baskets);
        model.addAttribute("counts", counts);
        model.addAttribute("user", user);
        return "/basket/user_basket";
    }

    @PostMapping("/basket")
    public String postMessageForBuyAll(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "numberPhone") String numberPhone,
            @RequestParam(name = "comment") String comment,
            Model model, Principal principal) {
        String way_to_get = "самовывоз";
        String payment_method = "наличными";
        int sumProduct = 112500;
        String listBuyProduct = "Принтер лазерный HP LaserJet PRO M104a (6000 р.)" + "\n" +
                "Ноутбук Acer NITROAn515-55 (84000 р.)" + "\n" +
                "Компьютер MacMini A1347 (22500 р.)";
        Usr user = getUserByPrincipal(principal);
        CompletedApplication completedApplication = new CompletedApplication();
        completedApplication.setName(name);
        completedApplication.setEmail(email);
        completedApplication.setComment(comment);
        completedApplication.setUsername(user.getUsername());
        completedApplication.setPayment_method(payment_method);
        completedApplication.setWay_to_get(way_to_get);
        completedApplication.setListBuyProduct(listBuyProduct);
        completedApplicationRepository.save(completedApplication);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String dateMessage = completedApplication.getDateOfCreated().toString().split("T")[0];
            String timeMessage = completedApplication.getDateOfCreated().toString().split("T")[1].split("\\.")[0];

            String messageClient = "Добрый день, " + user.getName()
                    + "!\nСпасибо за совершённую вами покупку.\n"
                    + "\nВаша заявка содержит следующие товары:\n" + "-".repeat(50)
                    + "\n\t *" + String.join("\n\t* ", listBuyProduct.split("\n"))
                    + "\nОбщая сумма товаров: " + sumProduct + " рублей"
                    + "\nВыбранный способ оплаты: \"" + payment_method + "\""
                    + "\nВыбранный способ получения: \"" + way_to_get + "\""
                    + "\n" + "-".repeat(50)
                    + "\nДата оформления: " + dateMessage + " и время оформления " + timeMessage
                    + "\n\nПо всем вопросам обращайтесь по номеру: " + Variables.companyNumber
                    + "\nИли напишите на нашу почту: " + "sar.comp.workshop@gmail.com"
                    + "\nМы находимся: Саратов, ул. Сакко и Ванцетти, д.47."
                    + "\nРаботаем Пн-Пт 9:00-18:00 (Сб, Вс - выходные)";
            System.out.println(messageClient);
            System.out.println();
            maiLSender.send(user.getEmail(), "Покупка товаров в \"" + Variables.companyName + "\" от " + dateMessage + " " + timeMessage, messageClient);

            String messageShop = "Заявка от " + user.getName()
                    + "\nEmail: " + user.getEmail()
                    + "\nНомер телефона: " + numberPhone
                    + "\nЗаявка содержит следующие товары:\n" + "-".repeat(50)
                    + "\n\t * " + String.join("\n\t * ", listBuyProduct.split("\n"))
                    + "\nОбщая сумма товаров: " + sumProduct + " рублей"
                    + "\nВыбранный способ оплаты: \"" + " наличными" + "\""
                    + "\nВыбранный способ получения: \"" + "самовывоз" + "\""
                    + "\nДата оформления: " + dateMessage + " и время оформления " + timeMessage;
            System.out.println(messageShop);
            maiLSender.send(Variables.companyEmail, "Заявка на покупку товаров от " + dateMessage + " " + timeMessage, messageShop);
        }
        return "/basket/user_basket";
    }
    // КОНТРОЛЛЕР для работы с изображениями
    @GetMapping("/images/notebook/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id){
        ImageNoteBook imageNoteBook = imageNoteBookRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .header("filename", imageNoteBook.getOriginalFileName())
                .contentType(MediaType.valueOf(imageNoteBook.getContentType()))
                .contentLength(imageNoteBook.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(imageNoteBook.getBytes())));
    }
    // прочие контролеры работы с изображениями

    // КОНТРОЛЛЕР для работы с ноутбуками
    @GetMapping("/notebooks/list")
    public String returnAllNoteBooksWithKeyword(@RequestParam(name = "keyword", required = false) String keyword, Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        System.out.println(keyword);
        Iterable<Notebook> notebooks = notebookService.listProducts(keyword);
        int counts = (int) notebooks.spliterator().getExactSizeIfKnown();
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("counts", counts);
        return "/products/notebook/notebooks_list";
    }

    //      Вывод каждого отдельного ноутбука
    @GetMapping("/notebook/{id}")
    public String notebookInfo(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        Optional<Notebook> notebook = notebookRepository.findById(id);
        ArrayList<Notebook> result = new ArrayList<>();
        notebook.ifPresent(result::add);
        model.addAttribute("product", result);
        model.addAttribute("name", result.get(0).getName().split("/")[0]);
        List<String> description = Arrays.asList(result.get(0).getDescription().split("\n"));
        List<String> descriptionOut = new ArrayList<>();
        description.forEach(item -> {
            if (item.length() > 1) descriptionOut.add(item);
        });
        model.addAttribute("description", descriptionOut);
        return "/products/notebook/notebook-info";
    }

    @PostMapping("/notebook/{id}/add_basket")
    public String saveNotebookInBasket(Model model, Principal principal, Notebook notebook) {
        Usr user = getUserByPrincipal(principal);
        Basket basket = new Basket();
        basket.setUserClass(user);
        basket.setNotebookClass(notebook);
        basketRepository.save(basket);
        model.addAttribute("message_basket", "");
        return "/products/notebook/notebooks_list";
    }

    // КОНТРОЛЛЕР для работы с пользователями
    @GetMapping("/login")
    public String loginUser(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "users/login";
    }

    @GetMapping("/registration")
    public String registrationUser(Model model, Principal principal) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        return "users/registration";
    }

    @PostMapping("/registration")
    public String createUser(Usr usr, Model model, @RequestParam(name = "confPassword", required = false) String confPassword) {
        if (!Objects.equals(usr.getPassword(), confPassword)) {
            model.addAttribute("message", "Пароли не совпадают. Пожалуйста, проверьте правильность введённых данных.");
            return "users/registration";
        }
        if (!userService.createUser(usr)) {
            model.addAttribute("message", "Такой аккаунт уже существует!");
            return "users/registration";
        }
        model.addAttribute("activate", "Активируйте аккаунт! Для этого перейдите по адресу на пришедщее сообщение.");
        return "users/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActive = userService.activateUser(code);
        if (isActive) {
            model.addAttribute("message", "Вы успешно активировали аккаунт");
        } else {
            model.addAttribute("message", "Код не действителен");
        }
        return "users/login";
    }

    @GetMapping("/account")
    public String getAccount(Principal principal, Model model) {
        Usr user = getUserByPrincipal(principal);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account_update_data")
    public String updateAccount(Principal principal, Model model, @RequestParam(name = "username") String username,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "phoneNumber") String phoneNumber) {
        System.out.println(username + " " + name + " " + email + " " + phoneNumber);
        Usr user = getUserByPrincipal(principal);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account_update_password")
    public String updatePasswordAccount(Principal principal, Model model,
                                        @RequestParam(name = "password") String password,
                                        @RequestParam(name = "new_password") String new_password,
                                        @RequestParam(name = "conf_new_password") String conf_new_password) {
        System.out.println(password + " " + new_password + " " + conf_new_password);
        Usr user = getUserByPrincipal(principal);
        if (Objects.equals(user.getPassword(), password)) {
            if (Objects.equals(new_password, conf_new_password)) {
                user.setPassword(new_password);
                userRepository.save(user);
                model.addAttribute("message_update_password", "Пароль успешно обновлён!");
            } else {
                model.addAttribute("message_update_password", "Введённый пароль неверен или новый пароль не соотвествует подтверждению!");
            }
        }
        model.addAttribute("show_result", true);
        model.addAttribute("check", user.getUsername() != null);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account_delete")
    public String deleteAccount(Principal principal, Model model,
                                @RequestParam(name = "username_check") String username,
                                @RequestParam(name = "password_check") String password) {
        System.out.println(username + " " + password);
        Usr user = getUserByPrincipal(principal);
        if (Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), password)) {
            userRepository.delete(user);
        }
        model.addAttribute("check", user.getUsername() != null);
        return "main/main";
    }

    public Usr getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new Usr();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
