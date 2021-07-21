package com.jkxy.car.api.controller;

import com.jkxy.car.api.pojo.Car;
import com.jkxy.car.api.service.CarService;
import com.jkxy.car.api.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static sun.java2d.xr.XRUtils.None;


@RestController
@RequestMapping("car")
public class CarController {
    @Autowired
    private CarService carService;

    /**
     * 购买汽车
     *
     * @param carSeries
     * @return
     */
    @GetMapping("buyCar/{carSeries}")
    public synchronized JSONResult buyCar(@PathVariable String carSeries) {
        List<Car> cars = carService.findBySeries(carSeries);
        if (cars == null || cars.size() == 0) {
            return JSONResult.errorMsg("Sorry, " + carSeries + " has already been sold out or we don't have that car in stock.");
        }
        Car c = cars.get(0);
        deleteById(c.getId());
        return JSONResult.ok("购买" + carSeries + "成功");
    }

    /**
     * 模糊查询
     * 任意返回数据从起止处
     *
     * @param keyword, start, end
     * @return
     */
    @GetMapping("fuzzySearch/{keyword}")
    public JSONResult fuzzySearch(@PathVariable String keyword, @RequestParam int start, @RequestParam int end) {
        List<Car> cars = carService.fuzzySearch(keyword);
        List<Car> res = new ArrayList<>();
        System.out.println(start + end);
        for (int i = cars.size() - 1; i >= max(0, start - 1); i--) {
            if (i <= end - 1) {
                res.add(cars.get(i));
            }
        }
        Collections.reverse(res);
        return JSONResult.ok(res);
    }

    /**
     * 查询所有汽车
     *
     * @return
     */
    @GetMapping("findAll")
    public JSONResult findAll() {
        List<Car> cars = carService.findAll();
        return JSONResult.ok(cars);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping("findById/{id}")
    public JSONResult findById(@PathVariable int id) {
        Car car = carService.findById(id);
        return JSONResult.ok(car);
    }

    /**
     * 通过车名查询
     *
     * @param carName
     * @return
     */
    @GetMapping("findByCarName/{carName}")
    public JSONResult findByCarName(@PathVariable String carName) {
        List<Car> cars = carService.findByCarName(carName);
        return JSONResult.ok(cars);
    }

    /**
     * 通过id执行删除
     *
     * @param id
     * @return
     */
    @GetMapping("deleteById/{id}")
    public JSONResult deleteById(@PathVariable int id) {
        carService.deleteById(id);
        return JSONResult.ok();
    }

    /**
     * 通过id更新全部信息
     *
     * @return
     */
    @PostMapping("updateById")
    public JSONResult updateById(Car car) {
        carService.updateById(car);
        return JSONResult.ok();
    }

    /**
     * 通过id增加
     *
     * @param car
     * @return
     */
    @PostMapping("insertCar")
    public JSONResult insertCar(Car car) {
        carService.insertCar(car);
        return JSONResult.ok();
    }
}
