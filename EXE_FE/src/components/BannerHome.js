import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getAllCampingSites,
  searchCampingInforsByName,
} from "../api/campingSiteService";
import axios from "axios";
import { Input, message } from "antd";
import { SearchOutlined } from "@ant-design/icons";

export default function BannerHome() {
  const formRef = useRef();
  const navigate = useNavigate();
  const [campingSites, setCampingSites] = useState([]);
  const [searchLocation, setSearchLocation] = useState("");
  const [selectedDestination, setSelectedDestination] = useState("");
  const fetched = useRef(false); // ✅ kiểm soát chỉ fetch 1 lần

  useEffect(() => {
    const fetchCampingSites = async () => {
      try {
        const data = await getAllCampingSites();
        setCampingSites(data || []);
      } catch (error) {
        console.error("❌ Lỗi khi tải danh sách camping:", error);
      }
    };
    fetchCampingSites();
  }, []);

  useEffect(() => {
    const loadScript = (src) =>
      new Promise((resolve, reject) => {
        const script = document.createElement("script");
        script.src = src;
        script.async = false;
        script.onload = resolve;
        script.onerror = reject;
        document.body.appendChild(script);
      });

    const loadScripts = async () => {
      try {
        await loadScript("/assets/js/jquery.datetimepicker.full.min.js");
        await loadScript("/assets/js/aos.js");

        if (window.$) {
          window.$(".datetimepicker").datetimepicker({
            format: "d/m/Y",
            timepicker: false,
          });
        }
        if (window.AOS) window.AOS.init();
      } catch (error) {
        console.error("❌ Failed to load script:", error);
      }
    };

    loadScripts();
  }, []);

  const handleSearchLocation = async (value) => {
    setSearchLocation(value);
    setSelectedDestination(""); // reset lựa chọn select

    if (!value.trim()) {
      message.warning("Vui lòng nhập tên địa điểm để tìm kiếm!");
      return;
    }

    try {
      const data = await searchCampingInforsByName(value);
      if (data && data.length > 0) {
        const first = data[0];
        const siteId = first.campingSiteId ?? first.id ?? first._id ?? null;
        navigate(`/tours?name=${encodeURIComponent(value)}&siteId=${siteId}`);
      } else {
        message.info("Không tìm thấy địa điểm nào phù hợp!");
      }
    } catch (error) {
      console.error("❌ Lỗi khi tìm kiếm địa điểm:", error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(formRef.current);
    const destination = formData.get("destination");

    if (!destination && !searchLocation.trim()) {
      message.warning("Vui lòng chọn hoặc nhập địa điểm để tìm kiếm!");
      return;
    }

    if (destination) {
      navigate(`/tours?siteId=${destination}`);
    } else {
      navigate(`/tours?name=${encodeURIComponent(searchLocation)}`);
    }
  };

  return (
    <section className="hero-area bgc-black pt-200 rpt-120 rel z-2">
      <div className="container-fluid" style={{ marginBottom: "40px" }}>
        <h1
          className="hero-title"
          style={{ marginTop: "100px" }}
          data-aos="flip-up"
          data-aos-delay="50"
          data-aos-duration="1500"
        >
          CAMPVERSE
        </h1>
        <div
          className="main-hero-image bgs-cover"
          style={{ backgroundImage: `url(/assets/images/hero/hero.jpg)` }}
        ></div>
      </div>

      <form ref={formRef} onSubmit={handleSubmit} id="search_form">
        <div className="container container-1400">
          <div
            className="search-filter-inner"
            data-aos="zoom-out-down"
            data-aos-duration="1500"
            style={{
              display: "flex",
              flexWrap: "wrap",
              gap: "15px",
              justifyContent: "center",
            }}
          >
            {/* 🔸 1️⃣ Cách 1: chọn điểm đến */}
            <div className="filter-item clearfix" style={{ flex: "1 1 250px" }}>
              <div className="icon">
                <i className="fal fa-map-marker-alt"></i>
              </div>
              <span className="title">Điểm đến</span>
              <select
                name="destination"
                id="destination"
                value={selectedDestination}
                onChange={(e) => {
                  setSelectedDestination(e.target.value);
                  setSearchLocation("");
                }}
              >
                <option value="">Chọn điểm đến</option>
                {campingSites.map((site) => {
                  const id = site.campingSiteId ?? site.id ?? site._id;
                  const label =
                    site.campingSiteName ??
                    site.name ??
                    site.location ??
                    site.address ??
                    id;
                  return (
                    <option key={id} value={id}>
                      {label}
                    </option>
                  );
                })}
              </select>
            </div>

            {/* 🔸 2️⃣ Cách 2: tìm kiếm theo tên */}
            <div className="filter-item clearfix" style={{ flex: "1 1 250px" }}>
              <div className="icon">
                <i className="fal fa-search-location"></i>
              </div>
              <span className="title">Tìm địa điểm camping</span>
              <Input.Search
                placeholder="Nhập tên địa điểm camping..."
                enterButton={<SearchOutlined />}
                size="large"
                allowClear
                value={searchLocation}
                onChange={(e) => setSearchLocation(e.target.value)}
                onSearch={handleSearchLocation}
              />
            </div>

            {/* 🔸 Nút tìm kiếm */}
            <div
              className="search-button"
              style={{
                flex: "0 1 150px",
                display: "flex",
                alignItems: "flex-end",
                justifyContent: "center",
              }}
            >
              <button className="theme-btn" type="submit">
                <span data-hover="Tìm kiếm">Tìm kiếm</span>
                <i className="far fa-search"></i>
              </button>
            </div>
          </div>
        </div>
      </form>
    </section>
  );
}
